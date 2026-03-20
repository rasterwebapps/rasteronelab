package com.rasteronelab.lis.admin.domain.repository;

import com.rasteronelab.lis.admin.domain.model.ReportTemplate;
import com.rasteronelab.lis.core.domain.repository.BranchAwareRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repository for ReportTemplate entity.
 * Extends BranchAwareRepository for branch-scoped queries with soft-delete filtering.
 */
@Repository
public interface ReportTemplateRepository extends BranchAwareRepository<ReportTemplate> {

    boolean existsByTemplateNameAndBranchIdAndIsDeletedFalse(String templateName, UUID branchId);

    Page<ReportTemplate> findAllByBranchIdAndIsDeletedFalseAndTemplateNameContainingIgnoreCase(UUID branchId, String templateName, Pageable pageable);
}
