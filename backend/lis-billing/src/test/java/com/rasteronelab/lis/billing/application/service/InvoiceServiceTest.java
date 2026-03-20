package com.rasteronelab.lis.billing.application.service;

import com.rasteronelab.lis.billing.api.dto.DiscountApplicationRequest;
import com.rasteronelab.lis.billing.api.dto.InvoiceLineItemRequest;
import com.rasteronelab.lis.billing.api.dto.InvoiceRequest;
import com.rasteronelab.lis.billing.api.dto.InvoiceResponse;
import com.rasteronelab.lis.billing.api.dto.OutstandingInvoiceResponse;
import com.rasteronelab.lis.billing.api.mapper.InvoiceLineItemMapper;
import com.rasteronelab.lis.billing.api.mapper.InvoiceMapper;
import com.rasteronelab.lis.billing.domain.model.Invoice;
import com.rasteronelab.lis.billing.domain.model.InvoiceLineItem;
import com.rasteronelab.lis.billing.domain.model.InvoiceStatus;
import com.rasteronelab.lis.billing.domain.repository.InvoiceRepository;
import com.rasteronelab.lis.core.common.exception.BusinessRuleException;
import com.rasteronelab.lis.core.common.exception.NotFoundException;
import com.rasteronelab.lis.core.infrastructure.BranchContextHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("InvoiceService")
@ExtendWith(MockitoExtension.class)
class InvoiceServiceTest {

    private static final UUID BRANCH_ID = UUID.randomUUID();

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private InvoiceMapper invoiceMapper;

    @Mock
    private InvoiceLineItemMapper lineItemMapper;

    @InjectMocks
    private InvoiceService invoiceService;

    @BeforeEach
    void setUp() {
        BranchContextHolder.setCurrentBranchId(BRANCH_ID);
    }

    @AfterEach
    void tearDown() {
        BranchContextHolder.clear();
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("generateInvoice should create invoice with line items and calculate totals")
    void generateInvoice_shouldCreateInvoiceWithTotals() {
        InvoiceLineItemRequest itemRequest = new InvoiceLineItemRequest();
        itemRequest.setTestCode("CBC");
        itemRequest.setTestName("Complete Blood Count");
        itemRequest.setQuantity(1);
        itemRequest.setUnitPrice(new BigDecimal("500.00"));
        itemRequest.setDiscountAmount(BigDecimal.ZERO);

        InvoiceRequest request = new InvoiceRequest();
        request.setOrderId(UUID.randomUUID());
        request.setPatientId(UUID.randomUUID());
        request.setLineItems(List.of(itemRequest));

        Invoice mappedInvoice = new Invoice();
        InvoiceLineItem mappedLineItem = new InvoiceLineItem();
        InvoiceResponse response = new InvoiceResponse();

        when(invoiceMapper.toEntity(request)).thenReturn(mappedInvoice);
        when(lineItemMapper.toEntity(itemRequest)).thenReturn(mappedLineItem);
        when(invoiceRepository.countByBranchIdAndIsDeletedFalse(BRANCH_ID)).thenReturn(0L);
        when(invoiceRepository.findByInvoiceNumberAndBranchIdAndIsDeletedFalse(any(), eq(BRANCH_ID)))
                .thenReturn(Optional.empty());
        when(invoiceRepository.save(any(Invoice.class))).thenReturn(mappedInvoice);
        when(invoiceMapper.toResponse(mappedInvoice)).thenReturn(response);

        InvoiceResponse result = invoiceService.generateInvoice(request);

        assertThat(result).isEqualTo(response);
        assertThat(mappedInvoice.getStatus()).isEqualTo(InvoiceStatus.GENERATED);
        assertThat(mappedInvoice.getBranchId()).isEqualTo(BRANCH_ID);
        assertThat(mappedInvoice.getSubtotal()).isEqualByComparingTo(new BigDecimal("500.00"));
        assertThat(mappedInvoice.getTotalAmount()).isEqualByComparingTo(new BigDecimal("500.00"));
        verify(invoiceRepository).save(any(Invoice.class));
    }

    @Test
    @DisplayName("getById should return invoice when found")
    void getById_shouldReturnInvoice() {
        UUID id = UUID.randomUUID();
        Invoice invoice = new Invoice();
        InvoiceResponse response = new InvoiceResponse();

        when(invoiceRepository.findByIdAndBranchIdAndIsDeletedFalse(id, BRANCH_ID))
                .thenReturn(Optional.of(invoice));
        when(invoiceMapper.toResponse(invoice)).thenReturn(response);

        InvoiceResponse result = invoiceService.getById(id);

        assertThat(result).isEqualTo(response);
    }

    @Test
    @DisplayName("getById should throw NotFoundException when not found")
    void getById_notFound_shouldThrowNotFoundException() {
        UUID id = UUID.randomUUID();

        when(invoiceRepository.findByIdAndBranchIdAndIsDeletedFalse(id, BRANCH_ID))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> invoiceService.getById(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Invoice");
    }

    @Test
    @DisplayName("applyDiscount should update invoice totals")
    void applyDiscount_shouldUpdateTotals() {
        UUID id = UUID.randomUUID();
        Invoice invoice = new Invoice();
        invoice.setSubtotal(new BigDecimal("1000.00"));
        invoice.setTaxAmount(BigDecimal.ZERO);
        invoice.setPaidAmount(BigDecimal.ZERO);
        invoice.setStatus(InvoiceStatus.GENERATED);

        Invoice saved = new Invoice();
        InvoiceResponse response = new InvoiceResponse();

        when(invoiceRepository.findByIdAndBranchIdAndIsDeletedFalse(id, BRANCH_ID))
                .thenReturn(Optional.of(invoice));
        when(invoiceRepository.save(invoice)).thenReturn(saved);
        when(invoiceMapper.toResponse(saved)).thenReturn(response);

        invoiceService.applyDiscount(id, "PERCENTAGE", new BigDecimal("100.00"), "Loyalty discount");

        assertThat(invoice.getTotalAmount()).isEqualByComparingTo(new BigDecimal("900.00"));
        assertThat(invoice.getBalanceAmount()).isEqualByComparingTo(new BigDecimal("900.00"));
    }

    @Test
    @DisplayName("delete should soft delete invoice")
    void delete_shouldSoftDelete() {
        UUID id = UUID.randomUUID();
        Invoice invoice = new Invoice();
        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken("admin", null));

        when(invoiceRepository.findByIdAndBranchIdAndIsDeletedFalse(id, BRANCH_ID))
                .thenReturn(Optional.of(invoice));

        invoiceService.delete(id);

        assertThat(invoice.getIsDeleted()).isTrue();
        assertThat(invoice.getDeletedAt()).isNotNull();
        verify(invoiceRepository).save(invoice);
    }

    @Test
    @DisplayName("applyDiscountScheme should apply percentage discount correctly")
    void applyDiscountScheme_shouldApplyPercentageDiscount() {
        UUID invoiceId = UUID.randomUUID();
        Invoice invoice = new Invoice();
        invoice.setSubtotal(new BigDecimal("1000.00"));
        invoice.setTaxAmount(new BigDecimal("50.00"));
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

        // 10% of 1000 = 100 discount; total = 1000 - 100 + 50 = 950
        assertThat(invoice.getDiscountAmount()).isEqualByComparingTo(new BigDecimal("100.00"));
        assertThat(invoice.getTotalAmount()).isEqualByComparingTo(new BigDecimal("950.00"));
        assertThat(invoice.getBalanceAmount()).isEqualByComparingTo(new BigDecimal("950.00"));
        assertThat(invoice.getDiscountType()).isEqualTo("PERCENTAGE");
        assertThat(invoice.getDiscountReason()).isEqualTo("Loyalty discount");
        verify(invoiceRepository).save(invoice);
    }

    @Test
    @DisplayName("applyDiscountScheme should apply flat discount correctly")
    void applyDiscountScheme_shouldApplyFlatDiscount() {
        UUID invoiceId = UUID.randomUUID();
        Invoice invoice = new Invoice();
        invoice.setSubtotal(new BigDecimal("1000.00"));
        invoice.setTaxAmount(BigDecimal.ZERO);
        invoice.setPaidAmount(new BigDecimal("200.00"));
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

        // Flat 150 discount; total = 1000 - 150 + 0 = 850; balance = 850 - 200 = 650
        assertThat(invoice.getDiscountAmount()).isEqualByComparingTo(new BigDecimal("150.00"));
        assertThat(invoice.getTotalAmount()).isEqualByComparingTo(new BigDecimal("850.00"));
        assertThat(invoice.getBalanceAmount()).isEqualByComparingTo(new BigDecimal("650.00"));
        assertThat(invoice.getDiscountType()).isEqualTo("FLAT");
        verify(invoiceRepository).save(invoice);
    }

    @Test
    @DisplayName("applyDiscountScheme should throw when invoice is PAID")
    void applyDiscountScheme_shouldThrowWhenInvoicePaid() {
        UUID invoiceId = UUID.randomUUID();
        Invoice invoice = new Invoice();
        invoice.setStatus(InvoiceStatus.PAID);

        when(invoiceRepository.findByIdAndBranchIdAndIsDeletedFalse(invoiceId, BRANCH_ID))
                .thenReturn(Optional.of(invoice));

        DiscountApplicationRequest request = DiscountApplicationRequest.builder()
                .invoiceId(invoiceId)
                .discountType("FLAT")
                .discountValue(new BigDecimal("50.00"))
                .build();

        assertThatThrownBy(() -> invoiceService.applyDiscountScheme(request))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("Cannot apply discount");
    }

    @Test
    @DisplayName("applyDiscountScheme should throw when discount exceeds subtotal")
    void applyDiscountScheme_shouldThrowWhenDiscountExceedsSubtotal() {
        UUID invoiceId = UUID.randomUUID();
        Invoice invoice = new Invoice();
        invoice.setSubtotal(new BigDecimal("100.00"));
        invoice.setTaxAmount(BigDecimal.ZERO);
        invoice.setPaidAmount(BigDecimal.ZERO);
        invoice.setStatus(InvoiceStatus.GENERATED);

        when(invoiceRepository.findByIdAndBranchIdAndIsDeletedFalse(invoiceId, BRANCH_ID))
                .thenReturn(Optional.of(invoice));

        DiscountApplicationRequest request = DiscountApplicationRequest.builder()
                .invoiceId(invoiceId)
                .discountType("FLAT")
                .discountValue(new BigDecimal("200.00"))
                .discountReason("Too large")
                .build();

        assertThatThrownBy(() -> invoiceService.applyDiscountScheme(request))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("exceeds invoice subtotal");
    }

    @Test
    @DisplayName("getOutstandingByPatient should return correct totals")
    void getOutstandingByPatient_shouldReturnCorrectTotals() {
        UUID patientId = UUID.randomUUID();

        Invoice inv1 = new Invoice();
        inv1.setInvoiceNumber("INV-001");
        inv1.setTotalAmount(new BigDecimal("500.00"));
        inv1.setBalanceAmount(new BigDecimal("300.00"));
        inv1.setDueDate(LocalDate.now().plusDays(30));

        Invoice inv2 = new Invoice();
        inv2.setInvoiceNumber("INV-002");
        inv2.setTotalAmount(new BigDecimal("200.00"));
        inv2.setBalanceAmount(new BigDecimal("200.00"));
        inv2.setDueDate(LocalDate.now().plusDays(15));

        when(invoiceRepository.findByBranchIdAndPatientIdAndStatusAndIsDeletedFalse(
                BRANCH_ID, patientId, InvoiceStatus.GENERATED))
                .thenReturn(List.of(inv1, inv2));

        OutstandingInvoiceResponse result = invoiceService.getOutstandingByPatient(patientId);

        assertThat(result.getPatientId()).isEqualTo(patientId);
        assertThat(result.getTotalOutstanding()).isEqualByComparingTo(new BigDecimal("500.00"));
        assertThat(result.getInvoiceCount()).isEqualTo(2);
        assertThat(result.getInvoices()).hasSize(2);
        assertThat(result.getInvoices().get(0).getInvoiceNumber()).isEqualTo("INV-001");
        assertThat(result.getInvoices().get(1).getBalanceAmount()).isEqualByComparingTo(new BigDecimal("200.00"));
    }
}
