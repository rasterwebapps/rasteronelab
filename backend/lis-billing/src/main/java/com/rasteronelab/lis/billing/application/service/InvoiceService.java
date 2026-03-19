package com.rasteronelab.lis.billing.application.service;

import com.rasteronelab.lis.billing.api.dto.InvoiceLineItemRequest;
import com.rasteronelab.lis.billing.api.dto.InvoiceRequest;
import com.rasteronelab.lis.billing.api.dto.InvoiceResponse;
import com.rasteronelab.lis.billing.api.mapper.InvoiceLineItemMapper;
import com.rasteronelab.lis.billing.api.mapper.InvoiceMapper;
import com.rasteronelab.lis.billing.domain.model.Invoice;
import com.rasteronelab.lis.billing.domain.model.InvoiceLineItem;
import com.rasteronelab.lis.billing.domain.model.InvoiceStatus;
import com.rasteronelab.lis.billing.domain.model.RateListType;
import com.rasteronelab.lis.billing.domain.repository.InvoiceRepository;
import com.rasteronelab.lis.core.common.exception.BusinessRuleException;
import com.rasteronelab.lis.core.common.exception.NotFoundException;
import com.rasteronelab.lis.core.infrastructure.BranchContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Service for Invoice operations.
 * All queries are branch-scoped via BranchContextHolder.
 */
@Service
@Transactional
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;
    private final InvoiceLineItemMapper lineItemMapper;

    public InvoiceService(InvoiceRepository invoiceRepository,
                          InvoiceMapper invoiceMapper,
                          InvoiceLineItemMapper lineItemMapper) {
        this.invoiceRepository = invoiceRepository;
        this.invoiceMapper = invoiceMapper;
        this.lineItemMapper = lineItemMapper;
    }

    public InvoiceResponse generateInvoice(InvoiceRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        Invoice invoice = invoiceMapper.toEntity(request);
        invoice.setBranchId(branchId);
        invoice.setIsActive(true);
        invoice.setInvoiceNumber(generateInvoiceNumber());
        invoice.setInvoiceDate(LocalDate.now());
        invoice.setDueDate(LocalDate.now().plusDays(30));
        invoice.setRateListType(request.getRateListType() != null
                ? request.getRateListType() : RateListType.WALK_IN);

        BigDecimal subtotal = BigDecimal.ZERO;
        if (request.getLineItems() != null) {
            for (InvoiceLineItemRequest itemRequest : request.getLineItems()) {
                InvoiceLineItem lineItem = lineItemMapper.toEntity(itemRequest);
                lineItem.setBranchId(branchId);
                lineItem.setIsActive(true);

                int qty = itemRequest.getQuantity() != null ? itemRequest.getQuantity() : 1;
                lineItem.setQuantity(qty);

                BigDecimal unitPrice = itemRequest.getUnitPrice() != null
                        ? itemRequest.getUnitPrice() : BigDecimal.ZERO;
                BigDecimal itemDiscount = itemRequest.getDiscountAmount() != null
                        ? itemRequest.getDiscountAmount() : BigDecimal.ZERO;
                BigDecimal netAmount = unitPrice.multiply(BigDecimal.valueOf(qty)).subtract(itemDiscount);
                lineItem.setNetAmount(netAmount);

                invoice.addLineItem(lineItem);
                subtotal = subtotal.add(netAmount);
            }
        }

        invoice.setSubtotal(subtotal);

        BigDecimal discountAmount = request.getDiscountAmount() != null
                ? request.getDiscountAmount() : BigDecimal.ZERO;
        invoice.setDiscountAmount(discountAmount);

        BigDecimal totalAmount = subtotal.subtract(discountAmount).add(
                invoice.getTaxAmount() != null ? invoice.getTaxAmount() : BigDecimal.ZERO);
        invoice.setTotalAmount(totalAmount);
        invoice.setPaidAmount(BigDecimal.ZERO);
        invoice.setBalanceAmount(totalAmount);
        invoice.setStatus(InvoiceStatus.GENERATED);

        Invoice saved = invoiceRepository.save(invoice);
        return invoiceMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public InvoiceResponse getById(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        Invoice invoice = invoiceRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("Invoice", id));
        return invoiceMapper.toResponse(invoice);
    }

    @Transactional(readOnly = true)
    public Page<InvoiceResponse> getAll(Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return invoiceRepository.findAllByBranchIdAndIsDeletedFalse(branchId, pageable)
                .map(invoiceMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<InvoiceResponse> getByPatient(UUID patientId, Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return invoiceRepository.findAllByPatientIdAndBranchIdAndIsDeletedFalse(patientId, branchId, pageable)
                .map(invoiceMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<InvoiceResponse> getByStatus(InvoiceStatus status, Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return invoiceRepository.findAllByStatusAndBranchIdAndIsDeletedFalse(status, branchId, pageable)
                .map(invoiceMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public InvoiceResponse getByOrder(UUID orderId) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        Invoice invoice = invoiceRepository.findByOrderIdAndBranchIdAndIsDeletedFalse(orderId, branchId)
                .orElseThrow(() -> new NotFoundException("Invoice", "orderId: " + orderId));
        return invoiceMapper.toResponse(invoice);
    }

    public InvoiceResponse applyDiscount(UUID invoiceId, String discountType,
                                         BigDecimal amount, String reason) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        Invoice invoice = invoiceRepository.findByIdAndBranchIdAndIsDeletedFalse(invoiceId, branchId)
                .orElseThrow(() -> new NotFoundException("Invoice", invoiceId));

        if (invoice.getStatus() == InvoiceStatus.PAID || invoice.getStatus() == InvoiceStatus.CANCELLED) {
            throw new BusinessRuleException("LIS-BIL-001",
                    "Cannot apply discount to invoice with status: " + invoice.getStatus());
        }

        invoice.setDiscountType(discountType);
        invoice.setDiscountAmount(amount);
        invoice.setDiscountReason(reason);

        BigDecimal totalAmount = invoice.getSubtotal().subtract(amount).add(
                invoice.getTaxAmount() != null ? invoice.getTaxAmount() : BigDecimal.ZERO);
        invoice.setTotalAmount(totalAmount);
        invoice.setBalanceAmount(totalAmount.subtract(invoice.getPaidAmount()));

        Invoice saved = invoiceRepository.save(invoice);
        return invoiceMapper.toResponse(saved);
    }

    public void delete(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        Invoice invoice = invoiceRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("Invoice", id));

        String currentUser = SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : "system";

        invoice.softDelete(currentUser);
        invoiceRepository.save(invoice);
    }

    private String generateInvoiceNumber() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        long count = invoiceRepository.countByBranchIdAndIsDeletedFalse(branchId);
        long sequence = count + 1;
        String invoiceNumber = String.format("INV-%s-%06d", datePart, sequence);

        while (invoiceRepository.findByInvoiceNumberAndBranchIdAndIsDeletedFalse(invoiceNumber, branchId).isPresent()) {
            sequence++;
            invoiceNumber = String.format("INV-%s-%06d", datePart, sequence);
        }

        return invoiceNumber;
    }
}
