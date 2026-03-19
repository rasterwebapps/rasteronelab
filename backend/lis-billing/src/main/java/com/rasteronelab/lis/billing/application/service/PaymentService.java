package com.rasteronelab.lis.billing.application.service;

import com.rasteronelab.lis.billing.api.dto.PaymentRequest;
import com.rasteronelab.lis.billing.api.dto.PaymentResponse;
import com.rasteronelab.lis.billing.api.mapper.PaymentMapper;
import com.rasteronelab.lis.billing.domain.model.Invoice;
import com.rasteronelab.lis.billing.domain.model.InvoiceStatus;
import com.rasteronelab.lis.billing.domain.model.Payment;
import com.rasteronelab.lis.billing.domain.repository.InvoiceRepository;
import com.rasteronelab.lis.billing.domain.repository.PaymentRepository;
import com.rasteronelab.lis.core.common.exception.BusinessRuleException;
import com.rasteronelab.lis.core.common.exception.NotFoundException;
import com.rasteronelab.lis.core.event.PaymentReceivedEvent;
import com.rasteronelab.lis.core.infrastructure.BranchContextHolder;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for Payment operations.
 * All queries are branch-scoped via BranchContextHolder.
 */
@Service
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;
    private final PaymentMapper paymentMapper;
    private final ApplicationEventPublisher eventPublisher;

    public PaymentService(PaymentRepository paymentRepository,
                          InvoiceRepository invoiceRepository,
                          PaymentMapper paymentMapper,
                          ApplicationEventPublisher eventPublisher) {
        this.paymentRepository = paymentRepository;
        this.invoiceRepository = invoiceRepository;
        this.paymentMapper = paymentMapper;
        this.eventPublisher = eventPublisher;
    }

    public PaymentResponse recordPayment(PaymentRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        Invoice invoice = invoiceRepository.findByIdAndBranchIdAndIsDeletedFalse(
                        request.getInvoiceId(), branchId)
                .orElseThrow(() -> new NotFoundException("Invoice", request.getInvoiceId()));

        if (invoice.getStatus() == InvoiceStatus.PAID) {
            throw new BusinessRuleException("LIS-BIL-002", "Invoice is already fully paid");
        }
        if (invoice.getStatus() == InvoiceStatus.CANCELLED) {
            throw new BusinessRuleException("LIS-BIL-003", "Cannot record payment for a cancelled invoice");
        }

        if (request.getAmount().compareTo(invoice.getBalanceAmount()) > 0) {
            throw new BusinessRuleException("LIS-BIL-004",
                    "Payment amount exceeds invoice balance of " + invoice.getBalanceAmount());
        }

        Payment payment = new Payment();
        payment.setBranchId(branchId);
        payment.setInvoice(invoice);
        payment.setReceiptNumber(generateReceiptNumber());
        payment.setAmount(request.getAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setTransactionRef(request.getTransactionRef());
        payment.setPaymentDate(LocalDateTime.now());
        payment.setNotes(request.getNotes());
        payment.setIsActive(true);

        String currentUser = SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : "system";
        payment.setReceivedBy(currentUser);

        // Update invoice amounts
        BigDecimal newPaidAmount = invoice.getPaidAmount().add(request.getAmount());
        BigDecimal newBalance = invoice.getTotalAmount().subtract(newPaidAmount);
        invoice.setPaidAmount(newPaidAmount);
        invoice.setBalanceAmount(newBalance);

        if (newBalance.compareTo(BigDecimal.ZERO) <= 0) {
            invoice.setStatus(InvoiceStatus.PAID);
        } else {
            invoice.setStatus(InvoiceStatus.PARTIALLY_PAID);
        }

        invoiceRepository.save(invoice);
        Payment saved = paymentRepository.save(payment);

        // Publish event for cross-module communication (order status update)
        boolean fullyPaid = newBalance.compareTo(BigDecimal.ZERO) <= 0;
        if (invoice.getOrderId() != null) {
            eventPublisher.publishEvent(new PaymentReceivedEvent(
                    invoice.getId(), invoice.getOrderId(), branchId, request.getAmount(), fullyPaid));
        }

        return paymentMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<PaymentResponse> getByInvoice(UUID invoiceId) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return paymentRepository.findAllByInvoiceIdAndBranchIdAndIsDeletedFalse(invoiceId, branchId)
                .stream()
                .map(paymentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PaymentResponse getById(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        Payment payment = paymentRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("Payment", id));
        return paymentMapper.toResponse(payment);
    }

    @Transactional(readOnly = true)
    public Page<PaymentResponse> getAll(Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return paymentRepository.findAllByBranchIdAndIsDeletedFalse(branchId, pageable)
                .map(paymentMapper::toResponse);
    }

    private String generateReceiptNumber() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        long count = paymentRepository.countByBranchIdAndIsDeletedFalse(branchId);
        long sequence = count + 1;
        String receiptNumber = String.format("RCP-%s-%06d", datePart, sequence);

        while (paymentRepository.findByReceiptNumberAndBranchIdAndIsDeletedFalse(receiptNumber, branchId).isPresent()) {
            sequence++;
            receiptNumber = String.format("RCP-%s-%06d", datePart, sequence);
        }

        return receiptNumber;
    }
}
