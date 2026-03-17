package com.rasteronelab.lis.admin.domain.repository;

import com.rasteronelab.lis.admin.domain.model.TestMaster;
import com.rasteronelab.lis.core.domain.repository.BranchAwareRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository for TestMaster entity.
 * Extends BranchAwareRepository for branch-scoped queries with soft-delete filtering.
 */
@Repository
public interface TestMasterRepository extends BranchAwareRepository<TestMaster> {

    Optional<TestMaster> findByCodeAndBranchIdAndIsDeletedFalse(String code, UUID branchId);

    boolean existsByCodeAndBranchIdAndIsDeletedFalse(String code, UUID branchId);

    Page<TestMaster> findAllByBranchIdAndDepartmentIdAndIsDeletedFalse(UUID branchId, UUID departmentId, Pageable pageable);

    Page<TestMaster> findAllByBranchIdAndIsActiveAndIsDeletedFalse(UUID branchId, Boolean isActive, Pageable pageable);

    Page<TestMaster> findAllByBranchIdAndIsDeletedFalseAndNameContainingIgnoreCase(UUID branchId, String name, Pageable pageable);
}
