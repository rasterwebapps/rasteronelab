package com.rasteronelab.lis.admin.domain.repository;

import com.rasteronelab.lis.admin.domain.model.Role;
import com.rasteronelab.lis.core.domain.repository.BranchAwareRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repository for Role entity.
 * Extends BranchAwareRepository for branch-scoped queries with soft-delete filtering.
 */
@Repository
public interface RoleRepository extends BranchAwareRepository<Role> {

    boolean existsByRoleNameAndBranchIdAndIsDeletedFalse(String roleName, UUID branchId);

    Page<Role> findAllByBranchIdAndIsDeletedFalseAndRoleNameContainingIgnoreCase(UUID branchId, String roleName, Pageable pageable);
}
