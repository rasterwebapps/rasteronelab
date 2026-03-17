package com.rasteronelab.lis.admin.domain.repository;

import com.rasteronelab.lis.admin.domain.model.BranchDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for BranchDepartment mapping entity.
 * All queries filter by isDeleted=false to enforce soft-delete semantics.
 */
@Repository
public interface BranchDepartmentRepository extends JpaRepository<BranchDepartment, UUID> {

    List<BranchDepartment> findAllByBranchIdAndIsDeletedFalse(UUID branchId);

    Optional<BranchDepartment> findByBranchIdAndDepartmentIdAndIsDeletedFalse(UUID branchId, UUID departmentId);

    boolean existsByBranchIdAndDepartmentIdAndIsDeletedFalse(UUID branchId, UUID departmentId);
}
