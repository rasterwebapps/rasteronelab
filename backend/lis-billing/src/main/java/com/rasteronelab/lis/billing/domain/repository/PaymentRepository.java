package com.rasteronelab.lis.billing.domain.repository;

import com.rasteronelab.lis.billing.domain.model.Payment;
import com.rasteronelab.lis.core.domain.repository.BranchAwareRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for Payment entity.
 * Extends BranchAwareRepository for branch-scoped queries with soft-delete filtering.
 */
@Repository
public interface PaymentRepository extends BranchAwareRepository<Payment> {

    List<Payment> findAllByInvoiceIdAndBranchIdAndIsDeletedFalse(UUID invoiceId, UUID branchId);

    Optional<Payment> findByReceiptNumberAndBranchIdAndIsDeletedFalse(String receiptNumber, UUID branchId);
}
