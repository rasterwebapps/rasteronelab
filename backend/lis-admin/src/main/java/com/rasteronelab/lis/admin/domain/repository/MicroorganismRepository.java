package com.rasteronelab.lis.admin.domain.repository;

import com.rasteronelab.lis.admin.domain.model.Microorganism;
import com.rasteronelab.lis.core.domain.repository.BranchAwareRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository for Microorganism entity.
 * Extends BranchAwareRepository for branch-scoped queries with soft-delete filtering.
 */
@Repository
public interface MicroorganismRepository extends BranchAwareRepository<Microorganism> {

    Optional<Microorganism> findByCodeAndBranchIdAndIsDeletedFalse(String code, UUID branchId);

    boolean existsByCodeAndBranchIdAndIsDeletedFalse(String code, UUID branchId);

    Page<Microorganism> findAllByBranchIdAndIsDeletedFalseAndNameContainingIgnoreCase(UUID branchId, String name, Pageable pageable);

    Page<Microorganism> findAllByBranchIdAndGramTypeAndIsDeletedFalse(UUID branchId, String gramType, Pageable pageable);
}
