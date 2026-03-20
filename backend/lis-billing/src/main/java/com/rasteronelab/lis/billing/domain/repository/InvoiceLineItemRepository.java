package com.rasteronelab.lis.billing.domain.repository;

import com.rasteronelab.lis.billing.domain.model.InvoiceLineItem;
import com.rasteronelab.lis.core.domain.repository.BranchAwareRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository for InvoiceLineItem entity.
 * Extends BranchAwareRepository for branch-scoped queries with soft-delete filtering.
 */
@Repository
public interface InvoiceLineItemRepository extends BranchAwareRepository<InvoiceLineItem> {

    List<InvoiceLineItem> findAllByInvoiceIdAndBranchIdAndIsDeletedFalse(UUID invoiceId, UUID branchId);
}
