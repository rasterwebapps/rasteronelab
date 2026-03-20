package com.rasteronelab.lis.billing.application.service;

import com.rasteronelab.lis.billing.api.dto.DiscountApplicationRequest;
import com.rasteronelab.lis.billing.api.dto.InvoiceLineItemRequest;
import com.rasteronelab.lis.billing.api.dto.InvoiceRequest;
import com.rasteronelab.lis.billing.api.dto.InvoiceResponse;
import com.rasteronelab.lis.billing.api.dto.PaymentRequest;
import com.rasteronelab.lis.billing.api.dto.PaymentResponse;
import com.rasteronelab.lis.billing.api.mapper.InvoiceLineItemMapper;
import com.rasteronelab.lis.billing.api.mapper.InvoiceMapper;
import com.rasteronelab.lis.billing.api.mapper.PaymentMapper;
import com.rasteronelab.lis.billing.application.listener.BillingEventListener;
import com.rasteronelab.lis.billing.domain.model.Invoice;
import com.rasteronelab.lis.billing.domain.model.InvoiceLineItem;
import com.rasteronelab.lis.billing.domain.model.InvoiceStatus;
import com.rasteronelab.lis.billing.domain.model.Payment;
import com.rasteronelab.lis.billing.domain.model.PaymentMethod;
import com.rasteronelab.lis.billing.domain.repository.InvoiceRepository;
import com.rasteronelab.lis.billing.domain.repository.PaymentRepository;
import com.rasteronelab.lis.core.common.exception.BusinessRuleException;
import com.rasteronelab.lis.core.event.OrderPlacedEvent;
import com.rasteronelab.lis.core.event.PaymentReceivedEvent;
import com.rasteronelab.lis.core.infrastructure.BranchContextHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Integration flow test covering the complete billing lifecycle:
 * invoice generation → discount application → payment recording → event publishing.
 */
@DisplayName("BillingFlow — end-to-end invoice → discount → payment → event flow")
@ExtendWith(MockitoExtension.class)
class BillingFlowTest {

    private static final UUID BRANCH_ID = UUID.randomUUID();

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private InvoiceMapper invoiceMapper;

    @Mock
    private InvoiceLineItemMapper lineItemMapper;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentMapper paymentMapper;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private InvoiceService invoiceService;

    @InjectMocks
    private PaymentService paymentService;

    private InvoiceService mockInvoiceService;
    private BillingEventListener billingEventListener;

    @Captor
    private ArgumentCaptor<PaymentReceivedEvent> eventCaptor;

    @BeforeEach
    void setUp() {
        BranchContextHolder.setCurrentBranchId(BRANCH_ID);
        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken("cashier", null));

        mockInvoiceService = mock(InvoiceService.class);
        billingEventListener = new BillingEventListener(mockInvoiceService);
    }

    @AfterEach
    void tearDown() {
        BranchContextHolder.clear();
        SecurityContextHolder.clearContext();
    }

    // ======================================================================
    // Test 1 — Event-driven invoice generation
    // ======================================================================

    @Test
    @DisplayName("OrderPlacedEvent triggers automatic invoice generation via event listener")
    void orderPlacedEvent_triggersInvoiceGeneration() {
        UUID orderId = UUID.randomUUID();
        UUID patientId = UUID.randomUUID();

        OrderPlacedEvent event = new OrderPlacedEvent(orderId, patientId, BRANCH_ID);
        InvoiceResponse response = new InvoiceResponse();

        when(mockInvoiceService.generateInvoice(argThat(req ->
                req.getOrderId().equals(orderId) && req.getPatientId().equals(patientId))))
                .thenReturn(response);

        billingEventListener.handleOrderPlaced(event);

        verify(mockInvoiceService).generateInvoice(argThat(req ->
                req.getOrderId().equals(orderId) && req.getPatientId().equals(patientId)));
    }

    // ======================================================================
    // Test 2 — Invoice generation with multiple line items
    // ======================================================================

    @Test
    @DisplayName("Invoice generation with 3 line items calculates correct subtotal and totals")
    void invoiceGeneration_withLineItems_calculatesCorrectTotals() {
        InvoiceLineItemRequest cbcItem = new InvoiceLineItemRequest();
        cbcItem.setTestCode("CBC");
        cbcItem.setTestName("Complete Blood Count");
        cbcItem.setQuantity(1);
        cbcItem.setUnitPrice(new BigDecimal("500.00"));
        cbcItem.setDiscountAmount(BigDecimal.ZERO);

        InvoiceLineItemRequest lipidItem = new InvoiceLineItemRequest();
        lipidItem.setTestCode("LIPID");
        lipidItem.setTestName("Lipid Panel");
        lipidItem.setQuantity(1);
        lipidItem.setUnitPrice(new BigDecimal("1200.00"));
        lipidItem.setDiscountAmount(new BigDecimal("100.00"));

        InvoiceLineItemRequest esrItem = new InvoiceLineItemRequest();
        esrItem.setTestCode("ESR");
        esrItem.setTestName("Erythrocyte Sedimentation Rate");
        esrItem.setQuantity(1);
        esrItem.setUnitPrice(new BigDecimal("200.00"));
        esrItem.setDiscountAmount(BigDecimal.ZERO);

        InvoiceRequest request = new InvoiceRequest();
        request.setOrderId(UUID.randomUUID());
        request.setPatientId(UUID.randomUUID());
        request.setLineItems(List.of(cbcItem, lipidItem, esrItem));

        Invoice mappedInvoice = new Invoice();
        InvoiceLineItem mappedCbc = new InvoiceLineItem();
        InvoiceLineItem mappedLipid = new InvoiceLineItem();
        InvoiceLineItem mappedEsr = new InvoiceLineItem();
        InvoiceResponse response = new InvoiceResponse();

        when(invoiceMapper.toEntity(request)).thenReturn(mappedInvoice);
        when(lineItemMapper.toEntity(cbcItem)).thenReturn(mappedCbc);
        when(lineItemMapper.toEntity(lipidItem)).thenReturn(mappedLipid);
        when(lineItemMapper.toEntity(esrItem)).thenReturn(mappedEsr);
        when(invoiceRepository.countByBranchIdAndIsDeletedFalse(BRANCH_ID)).thenReturn(0L);
        when(invoiceRepository.findByInvoiceNumberAndBranchIdAndIsDeletedFalse(any(), eq(BRANCH_ID)))
                .thenReturn(Optional.empty());
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(mappedInvoice);
        when(invoiceMapper.toResponse(mappedInvoice)).thenReturn(response);

        InvoiceResponse result = invoiceService.generateInvoice(request);

        assertThat(result).isEqualTo(response);
        // subtotal = 500 + (1200 - 100) + 200 = 1800; no invoice-level discount; total = 1800
        assertThat(mappedInvoice.getSubtotal()).isEqualByComparingTo(new BigDecimal("1800.00"));
        assertThat(mappedInvoice.getDiscountAmount()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(mappedInvoice.getTotalAmount()).isEqualByComparingTo(new BigDecimal("1800.00"));
        assertThat(mappedInvoice.getBalanceAmount()).isEqualByComparingTo(new BigDecimal("1800.00"));
        assertThat(mappedInvoice.getStatus()).isEqualTo(InvoiceStatus.GENERATED);
        assertThat(mappedInvoice.getLineItems()).hasSize(3);
        verify(invoiceRepository).save(any(Invoice.class));
    }

    // ======================================================================
    // Test 3 — Percentage discount application
    // ======================================================================

    @Test
    @DisplayName("Percentage discount recalculates invoice totals correctly")
    void applyPercentageDiscount_recalculatesInvoiceTotals() {
        UUID invoiceId = UUID.randomUUID();
        Invoice invoice = new Invoice();
        invoice.setSubtotal(new BigDecimal("1000.00"));
        invoice.setTaxAmount(BigDecimal.ZERO);
        invoice.setPaidAmount(BigDecimal.ZERO);
        invoice.setStatus(InvoiceStatus.GENERATED);

        Invoice saved = new Invoice();
        InvoiceResponse response = new InvoiceResponse();

        when(invoiceRepository.findByIdAndBranchIdAndIsDeletedFalse(invoiceId, BRANCH_ID))
                .thenReturn(Optional.of(invoice));
        when(invoiceRepository.save(invoice)).thenReturn(saved);
        when(invoiceMapper.toResponse(saved)).thenReturn(response);

        DiscountApplicationRequest request = DiscountApplicationRequest.builder()
                .invoiceId(invoiceId)
                .discountType("PERCENTAGE")
                .discountValue(new BigDecimal("10"))
                .discountReason("Loyalty discount")
                .build();

        invoiceService.applyDiscountScheme(request);

        // 10% of 1000 = 100 discount; total = 1000 - 100 + 0 = 900
        assertThat(invoice.getDiscountAmount()).isEqualByComparingTo(new BigDecimal("100.00"));
        assertThat(invoice.getTotalAmount()).isEqualByComparingTo(new BigDecimal("900.00"));
        assertThat(invoice.getBalanceAmount()).isEqualByComparingTo(new BigDecimal("900.00"));
        assertThat(invoice.getDiscountType()).isEqualTo("PERCENTAGE");
        assertThat(invoice.getDiscountReason()).isEqualTo("Loyalty discount");
        verify(invoiceRepository).save(invoice);
    }

    // ======================================================================
    // Test 4 — Flat discount application
    // ======================================================================

    @Test
    @DisplayName("Flat discount recalculates invoice totals correctly")
    void applyFlatDiscount_recalculatesInvoiceTotals() {
        UUID invoiceId = UUID.randomUUID();
        Invoice invoice = new Invoice();
        invoice.setSubtotal(new BigDecimal("1000.00"));
        invoice.setTaxAmount(BigDecimal.ZERO);
        invoice.setPaidAmount(BigDecimal.ZERO);
        invoice.setStatus(InvoiceStatus.GENERATED);

        Invoice saved = new Invoice();
        InvoiceResponse response = new InvoiceResponse();

        when(invoiceRepository.findByIdAndBranchIdAndIsDeletedFalse(invoiceId, BRANCH_ID))
                .thenReturn(Optional.of(invoice));
        when(invoiceRepository.save(invoice)).thenReturn(saved);
        when(invoiceMapper.toResponse(saved)).thenReturn(response);

        DiscountApplicationRequest request = DiscountApplicationRequest.builder()
                .invoiceId(invoiceId)
                .discountType("FLAT")
                .discountValue(new BigDecimal("150.00"))
                .discountReason("Promo code")
                .build();

        invoiceService.applyDiscountScheme(request);

        // Flat 150 discount; total = 1000 - 150 + 0 = 850; balance = 850 - 0 = 850
        assertThat(invoice.getDiscountAmount()).isEqualByComparingTo(new BigDecimal("150.00"));
        assertThat(invoice.getTotalAmount()).isEqualByComparingTo(new BigDecimal("850.00"));
        assertThat(invoice.getBalanceAmount()).isEqualByComparingTo(new BigDecimal("850.00"));
        assertThat(invoice.getDiscountType()).isEqualTo("FLAT");
        verify(invoiceRepository).save(invoice);
    }

    // ======================================================================
    // Test 5 — Full payment updates status to PAID and publishes event
    // ======================================================================

    @Test
    @DisplayName("Full payment sets invoice status to PAID and publishes PaymentReceivedEvent with fullyPaid=true")
    void fullPayment_updatesInvoiceStatusToPaid_publishesEvent() {
        UUID invoiceId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();

        Invoice invoice = new Invoice();
        invoice.setOrderId(orderId);
        invoice.setStatus(InvoiceStatus.GENERATED);
        invoice.setTotalAmount(new BigDecimal("1000.00"));
        invoice.setPaidAmount(BigDecimal.ZERO);
        invoice.setBalanceAmount(new BigDecimal("1000.00"));

        PaymentRequest request = new PaymentRequest();
        request.setInvoiceId(invoiceId);
        request.setAmount(new BigDecimal("1000.00"));
        request.setPaymentMethod(PaymentMethod.CASH);

        Payment saved = new Payment();
        PaymentResponse response = new PaymentResponse();

        when(invoiceRepository.findByIdAndBranchIdAndIsDeletedFalse(invoiceId, BRANCH_ID))
                .thenReturn(Optional.of(invoice));
        when(paymentRepository.countByBranchIdAndIsDeletedFalse(BRANCH_ID)).thenReturn(0L);
        when(paymentRepository.findByReceiptNumberAndBranchIdAndIsDeletedFalse(any(), eq(BRANCH_ID)))
                .thenReturn(Optional.empty());
        when(paymentRepository.save(any(Payment.class))).thenReturn(saved);
        when(paymentMapper.toResponse(saved)).thenReturn(response);

        paymentService.recordPayment(request);

        assertThat(invoice.getStatus()).isEqualTo(InvoiceStatus.PAID);
        assertThat(invoice.getPaidAmount()).isEqualByComparingTo(new BigDecimal("1000.00"));
        assertThat(invoice.getBalanceAmount()).isEqualByComparingTo(BigDecimal.ZERO);

        verify(eventPublisher).publishEvent(eventCaptor.capture());
        PaymentReceivedEvent event = eventCaptor.getValue();
        assertThat(event.getOrderId()).isEqualTo(orderId);
        assertThat(event.getBranchId()).isEqualTo(BRANCH_ID);
        assertThat(event.getAmount()).isEqualByComparingTo(new BigDecimal("1000.00"));
        assertThat(event.isFullyPaid()).isTrue();
    }

    // ======================================================================
    // Test 6 — Partial payment
    // ======================================================================

    @Test
    @DisplayName("Partial payment sets invoice status to PARTIALLY_PAID and publishes event with fullyPaid=false")
    void partialPayment_updatesInvoiceStatusToPartiallyPaid() {
        UUID invoiceId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();

        Invoice invoice = new Invoice();
        invoice.setOrderId(orderId);
        invoice.setStatus(InvoiceStatus.GENERATED);
        invoice.setTotalAmount(new BigDecimal("1000.00"));
        invoice.setPaidAmount(BigDecimal.ZERO);
        invoice.setBalanceAmount(new BigDecimal("1000.00"));

        PaymentRequest request = new PaymentRequest();
        request.setInvoiceId(invoiceId);
        request.setAmount(new BigDecimal("400.00"));
        request.setPaymentMethod(PaymentMethod.UPI);

        Payment saved = new Payment();
        PaymentResponse response = new PaymentResponse();

        when(invoiceRepository.findByIdAndBranchIdAndIsDeletedFalse(invoiceId, BRANCH_ID))
                .thenReturn(Optional.of(invoice));
        when(paymentRepository.countByBranchIdAndIsDeletedFalse(BRANCH_ID)).thenReturn(0L);
        when(paymentRepository.findByReceiptNumberAndBranchIdAndIsDeletedFalse(any(), eq(BRANCH_ID)))
                .thenReturn(Optional.empty());
        when(paymentRepository.save(any(Payment.class))).thenReturn(saved);
        when(paymentMapper.toResponse(saved)).thenReturn(response);

        paymentService.recordPayment(request);

        assertThat(invoice.getStatus()).isEqualTo(InvoiceStatus.PARTIALLY_PAID);
        assertThat(invoice.getPaidAmount()).isEqualByComparingTo(new BigDecimal("400.00"));
        assertThat(invoice.getBalanceAmount()).isEqualByComparingTo(new BigDecimal("600.00"));

        verify(eventPublisher).publishEvent(eventCaptor.capture());
        PaymentReceivedEvent event = eventCaptor.getValue();
        assertThat(event.isFullyPaid()).isFalse();
        assertThat(event.getAmount()).isEqualByComparingTo(new BigDecimal("400.00"));
    }

    // ======================================================================
    // Test 7 — Split payment (CASH then UPI) fully pays invoice
    // ======================================================================

    @Test
    @DisplayName("Split payment: CASH then UPI fully pays the invoice across two transactions")
    void splitPayment_cashThenUpi_fullyPaysInvoice() {
        UUID invoiceId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();

        Invoice invoice = new Invoice();
        invoice.setOrderId(orderId);
        invoice.setStatus(InvoiceStatus.GENERATED);
        invoice.setTotalAmount(new BigDecimal("1000.00"));
        invoice.setPaidAmount(BigDecimal.ZERO);
        invoice.setBalanceAmount(new BigDecimal("1000.00"));

        Payment savedPayment = new Payment();
        PaymentResponse paymentResponse = new PaymentResponse();

        when(invoiceRepository.findByIdAndBranchIdAndIsDeletedFalse(invoiceId, BRANCH_ID))
                .thenReturn(Optional.of(invoice));
        when(paymentRepository.countByBranchIdAndIsDeletedFalse(BRANCH_ID))
                .thenReturn(0L, 1L);
        when(paymentRepository.findByReceiptNumberAndBranchIdAndIsDeletedFalse(any(), eq(BRANCH_ID)))
                .thenReturn(Optional.empty());
        when(paymentRepository.save(any(Payment.class))).thenReturn(savedPayment);
        when(paymentMapper.toResponse(savedPayment)).thenReturn(paymentResponse);

        // First payment: 600 CASH → PARTIALLY_PAID
        PaymentRequest firstPayment = new PaymentRequest();
        firstPayment.setInvoiceId(invoiceId);
        firstPayment.setAmount(new BigDecimal("600.00"));
        firstPayment.setPaymentMethod(PaymentMethod.CASH);

        paymentService.recordPayment(firstPayment);

        assertThat(invoice.getStatus()).isEqualTo(InvoiceStatus.PARTIALLY_PAID);
        assertThat(invoice.getPaidAmount()).isEqualByComparingTo(new BigDecimal("600.00"));
        assertThat(invoice.getBalanceAmount()).isEqualByComparingTo(new BigDecimal("400.00"));

        // Second payment: 400 UPI → PAID
        PaymentRequest secondPayment = new PaymentRequest();
        secondPayment.setInvoiceId(invoiceId);
        secondPayment.setAmount(new BigDecimal("400.00"));
        secondPayment.setPaymentMethod(PaymentMethod.UPI);

        paymentService.recordPayment(secondPayment);

        assertThat(invoice.getStatus()).isEqualTo(InvoiceStatus.PAID);
        assertThat(invoice.getPaidAmount()).isEqualByComparingTo(new BigDecimal("1000.00"));
        assertThat(invoice.getBalanceAmount()).isEqualByComparingTo(BigDecimal.ZERO);

        verify(eventPublisher, times(2)).publishEvent(eventCaptor.capture());
        List<PaymentReceivedEvent> events = eventCaptor.getAllValues();
        assertThat(events.get(0).isFullyPaid()).isFalse();
        assertThat(events.get(0).getAmount()).isEqualByComparingTo(new BigDecimal("600.00"));
        assertThat(events.get(1).isFullyPaid()).isTrue();
        assertThat(events.get(1).getAmount()).isEqualByComparingTo(new BigDecimal("400.00"));
    }

    // ======================================================================
    // Test 8 — Payment exceeding balance throws exception
    // ======================================================================

    @Test
    @DisplayName("Payment exceeding invoice balance throws BusinessRuleException")
    void paymentExceedingBalance_throwsException() {
        UUID invoiceId = UUID.randomUUID();

        Invoice invoice = new Invoice();
        invoice.setStatus(InvoiceStatus.PARTIALLY_PAID);
        invoice.setTotalAmount(new BigDecimal("1000.00"));
        invoice.setPaidAmount(new BigDecimal("500.00"));
        invoice.setBalanceAmount(new BigDecimal("500.00"));

        when(invoiceRepository.findByIdAndBranchIdAndIsDeletedFalse(invoiceId, BRANCH_ID))
                .thenReturn(Optional.of(invoice));

        PaymentRequest request = new PaymentRequest();
        request.setInvoiceId(invoiceId);
        request.setAmount(new BigDecimal("600.00"));
        request.setPaymentMethod(PaymentMethod.CASH);

        assertThatThrownBy(() -> paymentService.recordPayment(request))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("exceeds invoice balance");
    }

    // ======================================================================
    // Test 9 — Discount exceeding subtotal throws exception
    // ======================================================================

    @Test
    @DisplayName("Flat discount exceeding subtotal throws BusinessRuleException")
    void discountExceedingSubtotal_throwsException() {
        UUID invoiceId = UUID.randomUUID();

        Invoice invoice = new Invoice();
        invoice.setSubtotal(new BigDecimal("500.00"));
        invoice.setTaxAmount(BigDecimal.ZERO);
        invoice.setPaidAmount(BigDecimal.ZERO);
        invoice.setStatus(InvoiceStatus.GENERATED);

        when(invoiceRepository.findByIdAndBranchIdAndIsDeletedFalse(invoiceId, BRANCH_ID))
                .thenReturn(Optional.of(invoice));

        DiscountApplicationRequest request = DiscountApplicationRequest.builder()
                .invoiceId(invoiceId)
                .discountType("FLAT")
                .discountValue(new BigDecimal("600.00"))
                .discountReason("Too generous")
                .build();

        assertThatThrownBy(() -> invoiceService.applyDiscountScheme(request))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("exceeds invoice subtotal");
    }

    // ======================================================================
    // Test 10 — Payment on PAID invoice throws exception
    // ======================================================================

    @Test
    @DisplayName("Recording payment on a fully paid invoice throws BusinessRuleException")
    void paymentOnPaidInvoice_throwsException() {
        UUID invoiceId = UUID.randomUUID();

        Invoice invoice = new Invoice();
        invoice.setStatus(InvoiceStatus.PAID);
        invoice.setTotalAmount(new BigDecimal("1000.00"));
        invoice.setPaidAmount(new BigDecimal("1000.00"));
        invoice.setBalanceAmount(BigDecimal.ZERO);

        when(invoiceRepository.findByIdAndBranchIdAndIsDeletedFalse(invoiceId, BRANCH_ID))
                .thenReturn(Optional.of(invoice));

        PaymentRequest request = new PaymentRequest();
        request.setInvoiceId(invoiceId);
        request.setAmount(new BigDecimal("100.00"));
        request.setPaymentMethod(PaymentMethod.CASH);

        assertThatThrownBy(() -> paymentService.recordPayment(request))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("already fully paid");
    }
}
