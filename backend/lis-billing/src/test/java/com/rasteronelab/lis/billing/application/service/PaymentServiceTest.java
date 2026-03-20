package com.rasteronelab.lis.billing.application.service;

import com.rasteronelab.lis.billing.api.dto.PaymentRequest;
import com.rasteronelab.lis.billing.api.dto.PaymentResponse;
import com.rasteronelab.lis.billing.api.mapper.PaymentMapper;
import com.rasteronelab.lis.billing.domain.model.Invoice;
import com.rasteronelab.lis.billing.domain.model.InvoiceStatus;
import com.rasteronelab.lis.billing.domain.model.Payment;
import com.rasteronelab.lis.billing.domain.model.PaymentMethod;
import com.rasteronelab.lis.billing.domain.repository.InvoiceRepository;
import com.rasteronelab.lis.billing.domain.repository.PaymentRepository;
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
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("PaymentService")
@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    private static final UUID BRANCH_ID = UUID.randomUUID();

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private PaymentMapper paymentMapper;

    @InjectMocks
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        BranchContextHolder.setCurrentBranchId(BRANCH_ID);
        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken("cashier", null));
    }

    @AfterEach
    void tearDown() {
        BranchContextHolder.clear();
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("recordPayment should save payment and update invoice")
    void recordPayment_shouldSavePaymentAndUpdateInvoice() {
        UUID invoiceId = UUID.randomUUID();
        Invoice invoice = new Invoice();
        invoice.setStatus(InvoiceStatus.GENERATED);
        invoice.setTotalAmount(new BigDecimal("1000.00"));
        invoice.setPaidAmount(BigDecimal.ZERO);
        invoice.setBalanceAmount(new BigDecimal("1000.00"));

        PaymentRequest request = new PaymentRequest();
        request.setInvoiceId(invoiceId);
        request.setAmount(new BigDecimal("500.00"));
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

        PaymentResponse result = paymentService.recordPayment(request);

        assertThat(result).isEqualTo(response);
        assertThat(invoice.getPaidAmount()).isEqualByComparingTo(new BigDecimal("500.00"));
        assertThat(invoice.getBalanceAmount()).isEqualByComparingTo(new BigDecimal("500.00"));
        assertThat(invoice.getStatus()).isEqualTo(InvoiceStatus.PARTIALLY_PAID);
        verify(invoiceRepository).save(invoice);
    }

    @Test
    @DisplayName("recordPayment with full amount should set invoice status to PAID")
    void recordPayment_fullPayment_shouldSetStatusPaid() {
        UUID invoiceId = UUID.randomUUID();
        Invoice invoice = new Invoice();
        invoice.setStatus(InvoiceStatus.GENERATED);
        invoice.setTotalAmount(new BigDecimal("1000.00"));
        invoice.setPaidAmount(BigDecimal.ZERO);
        invoice.setBalanceAmount(new BigDecimal("1000.00"));

        PaymentRequest request = new PaymentRequest();
        request.setInvoiceId(invoiceId);
        request.setAmount(new BigDecimal("1000.00"));
        request.setPaymentMethod(PaymentMethod.CARD);

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
        assertThat(invoice.getBalanceAmount()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("recordPayment partial amount should set invoice status to PARTIALLY_PAID")
    void recordPayment_partialPayment_shouldSetStatusPartiallyPaid() {
        UUID invoiceId = UUID.randomUUID();
        Invoice invoice = new Invoice();
        invoice.setStatus(InvoiceStatus.GENERATED);
        invoice.setTotalAmount(new BigDecimal("1000.00"));
        invoice.setPaidAmount(BigDecimal.ZERO);
        invoice.setBalanceAmount(new BigDecimal("1000.00"));

        PaymentRequest request = new PaymentRequest();
        request.setInvoiceId(invoiceId);
        request.setAmount(new BigDecimal("300.00"));
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
        assertThat(invoice.getPaidAmount()).isEqualByComparingTo(new BigDecimal("300.00"));
        assertThat(invoice.getBalanceAmount()).isEqualByComparingTo(new BigDecimal("700.00"));
    }
}
