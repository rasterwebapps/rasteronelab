package com.rasteronelab.lis.order.domain.repository;

import com.rasteronelab.lis.core.domain.repository.BranchAwareRepository;
import com.rasteronelab.lis.order.domain.model.OrderLineItem;

import java.util.List;
import java.util.UUID;

/**
 * Repository for OrderLineItem entity.
 * All queries are branch-scoped and exclude soft-deleted records.
 */
public interface OrderLineItemRepository extends BranchAwareRepository<OrderLineItem> {

    List<OrderLineItem> findAllByOrderIdAndBranchIdAndIsDeletedFalse(UUID orderId, UUID branchId);
}
