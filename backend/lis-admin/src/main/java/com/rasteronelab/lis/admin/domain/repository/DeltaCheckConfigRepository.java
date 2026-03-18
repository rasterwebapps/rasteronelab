package com.rasteronelab.lis.admin.domain.repository;

import com.rasteronelab.lis.admin.domain.model.DeltaCheckConfig;
import com.rasteronelab.lis.core.domain.repository.BranchAwareRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository for DeltaCheckConfig entity.
 * Extends BranchAwareRepository for branch-scoped queries with soft-delete filtering.
 */
@Repository
public interface DeltaCheckConfigRepository extends BranchAwareRepository<DeltaCheckConfig> {

    List<DeltaCheckConfig> findAllByBranchIdAndParameterIdAndIsDeletedFalse(UUID branchId, UUID parameterId);
}
