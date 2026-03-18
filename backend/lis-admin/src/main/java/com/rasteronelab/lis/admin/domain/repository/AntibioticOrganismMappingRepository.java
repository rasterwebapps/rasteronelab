package com.rasteronelab.lis.admin.domain.repository;

import com.rasteronelab.lis.admin.domain.model.AntibioticOrganismMapping;
import com.rasteronelab.lis.core.domain.repository.BranchAwareRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repository for AntibioticOrganismMapping entity.
 * Extends BranchAwareRepository for branch-scoped queries with soft-delete filtering.
 */
@Repository
public interface AntibioticOrganismMappingRepository extends BranchAwareRepository<AntibioticOrganismMapping> {

    Page<AntibioticOrganismMapping> findAllByBranchIdAndAntibioticIdAndIsDeletedFalse(UUID branchId, UUID antibioticId, Pageable pageable);

    Page<AntibioticOrganismMapping> findAllByBranchIdAndMicroorganismIdAndIsDeletedFalse(UUID branchId, UUID microorganismId, Pageable pageable);

    boolean existsByBranchIdAndAntibioticIdAndMicroorganismIdAndIsDeletedFalse(UUID branchId, UUID antibioticId, UUID microorganismId);
}
