package com.rasteronelab.lis.admin.domain.repository;

import com.rasteronelab.lis.admin.domain.model.TestPanel;
import com.rasteronelab.lis.core.domain.repository.BranchAwareRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository for TestPanel entity.
 * Extends BranchAwareRepository for branch-scoped queries with soft-delete filtering.
 */
@Repository
public interface TestPanelRepository extends BranchAwareRepository<TestPanel> {

    Optional<TestPanel> findByCodeAndBranchIdAndIsDeletedFalse(String code, UUID branchId);

    boolean existsByCodeAndBranchIdAndIsDeletedFalse(String code, UUID branchId);

    Page<TestPanel> findAllByBranchIdAndIsActiveAndIsDeletedFalse(UUID branchId, Boolean isActive, Pageable pageable);
}
