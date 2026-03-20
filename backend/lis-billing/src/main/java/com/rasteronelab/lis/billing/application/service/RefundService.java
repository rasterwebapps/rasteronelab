package com.rasteronelab.lis.billing.application.service;

import com.rasteronelab.lis.billing.api.dto.RefundRequest;
import com.rasteronelab.lis.billing.api.dto.RefundResponse;
import com.rasteronelab.lis.billing.api.mapper.RefundMapper;
import com.rasteronelab.lis.billing.domain.model.Invoice;
import com.rasteronelab.lis.billing.domain.model.InvoiceStatus;
import com.rasteronelab.lis.billing.domain.model.Refund;
import com.rasteronelab.lis.billing.domain.model.RefundStatus;
import com.rasteronelab.lis.billing.domain.repository.InvoiceRepository;
import com.rasteronelab.lis.billing.domain.repository.RefundRepository;
import com.rasteronelab.lis.core.common.exception.BusinessRuleException;
import com.rasteronelab.lis.core.common.exception.NotFoundException;
import com.rasteronelab.lis.core.infrastructure.BranchContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
 * Service for Refund operations.
 * All queries are branch-scoped via BranchContextHolder.
 */
@Service
@Transactional
public class RefundService {

    private final RefundRepository refundRepository;
    private final InvoiceRepository invoiceRepository;
    private final RefundMapper refundMapper;

    public RefundService(RefundRepository refundRepository,
                         InvoiceRepository invoiceRepository,
                         RefundMapper refundMapper) {
        this.refundRepository = refundRepository;
        this.invoiceRepository = invoiceRepository;
        this.refundMapper = refundMapper;
    }

    public RefundResponse requestRefund(RefundRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        Invoice invoice = invoiceRepository.findByIdAndBranchIdAndIsDeletedFalse(
                        request.getInvoiceId(), branchId)
                .orElseThrow(() -> new NotFoundException("Invoice", request.getInvoiceId()));

        if (request.getAmount().compareTo(invoice.getPaidAmount()) > 0) {
            throw new BusinessRuleException("LIS-BIL-005",
                    "Refund amount exceeds paid amount of " + invoice.getPaidAmount());
        }

        Refund refund = new Refund();
        refund.setBranchId(branchId);
        refund.setInvoice(invoice);
        refund.setPaymentId(request.getPaymentId());
        refund.setRefundNumber(generateRefundNumber());
        refund.setAmount(request.getAmount());
        refund.setReason(request.getReason());
        refund.setRefundMethod(request.getRefundMethod());
        refund.setStatus(RefundStatus.REQUESTED);
        refund.setIsActive(true);

        Refund saved = refundRepository.save(refund);
        return refundMapper.toResponse(saved);
    }

    public RefundResponse approveRefund(UUID refundId, String approvedBy) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        Refund refund = refundRepository.findByIdAndBranchIdAndIsDeletedFalse(refundId, branchId)
                .orElseThrow(() -> new NotFoundException("Refund", refundId));

        if (refund.getStatus() != RefundStatus.REQUESTED) {
            throw new BusinessRuleException("LIS-BIL-006",
                    "Only REQUESTED refunds can be approved. Current status: " + refund.getStatus());
        }

        refund.setStatus(RefundStatus.APPROVED);
        refund.setApprovedBy(approvedBy);
        refund.setApprovedAt(LocalDateTime.now());

        Refund saved = refundRepository.save(refund);
        return refundMapper.toResponse(saved);
    }

    public RefundResponse processRefund(UUID refundId) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        Refund refund = refundRepository.findByIdAndBranchIdAndIsDeletedFalse(refundId, branchId)
                .orElseThrow(() -> new NotFoundException("Refund", refundId));

        if (refund.getStatus() != RefundStatus.APPROVED) {
            throw new BusinessRuleException("LIS-BIL-007",
                    "Only APPROVED refunds can be processed. Current status: " + refund.getStatus());
        }

        refund.setStatus(RefundStatus.PROCESSED);
        refund.setRefundDate(LocalDateTime.now());

        // Update invoice amounts
        Invoice invoice = refund.getInvoice();
        BigDecimal newPaidAmount = invoice.getPaidAmount().subtract(refund.getAmount());
        invoice.setPaidAmount(newPaidAmount);
        invoice.setBalanceAmount(invoice.getTotalAmount().subtract(newPaidAmount));

        if (newPaidAmount.compareTo(BigDecimal.ZERO) <= 0) {
            invoice.setStatus(InvoiceStatus.REFUNDED);
        } else {
            invoice.setStatus(InvoiceStatus.PARTIALLY_PAID);
        }

        invoiceRepository.save(invoice);
        Refund saved = refundRepository.save(refund);
        return refundMapper.toResponse(saved);
    }

    public RefundResponse rejectRefund(UUID refundId, String reason) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        Refund refund = refundRepository.findByIdAndBranchIdAndIsDeletedFalse(refundId, branchId)
                .orElseThrow(() -> new NotFoundException("Refund", refundId));

        if (refund.getStatus() != RefundStatus.REQUESTED) {
            throw new BusinessRuleException("LIS-BIL-008",
                    "Only REQUESTED refunds can be rejected. Current status: " + refund.getStatus());
        }

        refund.setStatus(RefundStatus.REJECTED);
        refund.setNotes(reason);

        Refund saved = refundRepository.save(refund);
        return refundMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<RefundResponse> getByInvoice(UUID invoiceId) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return refundRepository.findAllByInvoiceIdAndBranchIdAndIsDeletedFalse(invoiceId, branchId)
                .stream()
                .map(refundMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<RefundResponse> getAll(Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return refundRepository.findAllByBranchIdAndIsDeletedFalse(branchId, pageable)
                .map(refundMapper::toResponse);
    }

    private String generateRefundNumber() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        long count = refundRepository.countByBranchIdAndIsDeletedFalse(branchId);
        return String.format("RFD-%s-%06d", datePart, count + 1);
    }
}
