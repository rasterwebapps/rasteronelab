package com.rasteronelab.lis.admin.domain.repository;

import com.rasteronelab.lis.admin.domain.model.AutoValidationRule;
import com.rasteronelab.lis.core.domain.repository.BranchAwareRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository for AutoValidationRule entity.
 * Extends BranchAwareRepository for branch-scoped queries with soft-delete filtering.
 */
@Repository
public interface AutoValidationRuleRepository extends BranchAwareRepository<AutoValidationRule> {

    List<AutoValidationRule> findAllByBranchIdAndTestIdAndIsDeletedFalse(UUID branchId, UUID testId);

    List<AutoValidationRule> findAllByBranchIdAndParameterIdAndIsDeletedFalse(UUID branchId, UUID parameterId);

    List<AutoValidationRule> findAllByBranchIdAndRuleTypeAndIsDeletedFalse(UUID branchId, String ruleType);
}
