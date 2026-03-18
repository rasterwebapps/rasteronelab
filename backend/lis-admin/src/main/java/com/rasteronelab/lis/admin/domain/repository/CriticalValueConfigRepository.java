package com.rasteronelab.lis.admin.domain.repository;

import com.rasteronelab.lis.admin.domain.model.CriticalValueConfig;
import com.rasteronelab.lis.core.domain.repository.BranchAwareRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository for CriticalValueConfig entity.
 * Extends BranchAwareRepository for branch-scoped queries with soft-delete filtering.
 */
@Repository
public interface CriticalValueConfigRepository extends BranchAwareRepository<CriticalValueConfig> {

    List<CriticalValueConfig> findAllByBranchIdAndParameterIdAndIsDeletedFalse(UUID branchId, UUID parameterId);
}
