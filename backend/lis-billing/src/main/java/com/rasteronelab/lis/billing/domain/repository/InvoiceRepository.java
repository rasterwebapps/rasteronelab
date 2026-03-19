package com.rasteronelab.lis.billing.domain.repository;

import com.rasteronelab.lis.billing.domain.model.Invoice;
import com.rasteronelab.lis.billing.domain.model.InvoiceStatus;
import com.rasteronelab.lis.core.domain.repository.BranchAwareRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository for Invoice entity.
 * Extends BranchAwareRepository for branch-scoped queries with soft-delete filtering.
 */
@Repository
public interface InvoiceRepository extends BranchAwareRepository<Invoice> {

    Optional<Invoice> findByInvoiceNumberAndBranchIdAndIsDeletedFalse(String invoiceNumber, UUID branchId);

    Optional<Invoice> findByOrderIdAndBranchIdAndIsDeletedFalse(UUID orderId, UUID branchId);

    Page<Invoice> findAllByPatientIdAndBranchIdAndIsDeletedFalse(UUID patientId, UUID branchId, Pageable pageable);

    Page<Invoice> findAllByStatusAndBranchIdAndIsDeletedFalse(InvoiceStatus status, UUID branchId, Pageable pageable);
}
