package com.rasteronelab.lis.admin.domain.repository;

import com.rasteronelab.lis.admin.domain.model.Antibiotic;
import com.rasteronelab.lis.core.domain.repository.BranchAwareRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository for Antibiotic entity.
 * Extends BranchAwareRepository for branch-scoped queries with soft-delete filtering.
 */
@Repository
public interface AntibioticRepository extends BranchAwareRepository<Antibiotic> {

    Optional<Antibiotic> findByCodeAndBranchIdAndIsDeletedFalse(String code, UUID branchId);

    boolean existsByCodeAndBranchIdAndIsDeletedFalse(String code, UUID branchId);

    Page<Antibiotic> findAllByBranchIdAndIsDeletedFalseAndNameContainingIgnoreCase(UUID branchId, String name, Pageable pageable);

    Page<Antibiotic> findAllByBranchIdAndAntibioticGroupAndIsDeletedFalse(UUID branchId, String antibioticGroup, Pageable pageable);
}
