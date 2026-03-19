package com.rasteronelab.lis.billing.domain.repository;

import com.rasteronelab.lis.billing.domain.model.Refund;
import com.rasteronelab.lis.billing.domain.model.RefundStatus;
import com.rasteronelab.lis.core.domain.repository.BranchAwareRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository for Refund entity.
 * Extends BranchAwareRepository for branch-scoped queries with soft-delete filtering.
 */
@Repository
public interface RefundRepository extends BranchAwareRepository<Refund> {

    List<Refund> findAllByInvoiceIdAndBranchIdAndIsDeletedFalse(UUID invoiceId, UUID branchId);

    Page<Refund> findAllByStatusAndBranchIdAndIsDeletedFalse(RefundStatus status, UUID branchId, Pageable pageable);
}
