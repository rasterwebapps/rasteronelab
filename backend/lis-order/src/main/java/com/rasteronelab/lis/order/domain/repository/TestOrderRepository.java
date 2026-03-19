package com.rasteronelab.lis.order.domain.repository;

import com.rasteronelab.lis.core.domain.repository.BranchAwareRepository;
import com.rasteronelab.lis.order.domain.model.OrderStatus;
import com.rasteronelab.lis.order.domain.model.TestOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for TestOrder entity.
 * All queries are branch-scoped and exclude soft-deleted records.
 */
public interface TestOrderRepository extends BranchAwareRepository<TestOrder> {

    Optional<TestOrder> findByOrderNumberAndBranchIdAndIsDeletedFalse(String orderNumber, UUID branchId);

    Page<TestOrder> findAllByPatientIdAndBranchIdAndIsDeletedFalse(UUID patientId, UUID branchId, Pageable pageable);

    Page<TestOrder> findAllByStatusAndBranchIdAndIsDeletedFalse(OrderStatus status, UUID branchId, Pageable pageable);

    boolean existsByOrderNumberAndBranchIdAndIsDeletedFalse(String orderNumber, UUID branchId);

    long countByBranchIdAndIsDeletedFalseAndOrderDateBetween(UUID branchId, LocalDateTime from, LocalDateTime to);
}
