package com.rasteronelab.lis.admin.domain.repository;

import com.rasteronelab.lis.admin.domain.model.InsuranceTariff;
import com.rasteronelab.lis.core.domain.repository.BranchAwareRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repository for InsuranceTariff entity.
 * Extends BranchAwareRepository for branch-scoped queries with soft-delete filtering.
 */
@Repository
public interface InsuranceTariffRepository extends BranchAwareRepository<InsuranceTariff> {

    boolean existsByInsuranceNameAndPlanNameAndBranchIdAndIsDeletedFalse(String insuranceName, String planName, UUID branchId);

    Page<InsuranceTariff> findAllByBranchIdAndIsDeletedFalseAndInsuranceNameContainingIgnoreCase(UUID branchId, String insuranceName, Pageable pageable);
}
