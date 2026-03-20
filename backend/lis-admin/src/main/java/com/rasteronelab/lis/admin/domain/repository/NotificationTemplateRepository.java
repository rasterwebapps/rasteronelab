package com.rasteronelab.lis.admin.domain.repository;

import com.rasteronelab.lis.admin.domain.model.NotificationTemplate;
import com.rasteronelab.lis.core.domain.repository.BranchAwareRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository for NotificationTemplate entity.
 * Extends BranchAwareRepository for branch-scoped queries with soft-delete filtering.
 */
@Repository
public interface NotificationTemplateRepository extends BranchAwareRepository<NotificationTemplate> {

    Optional<NotificationTemplate> findByTemplateCodeAndBranchIdAndIsDeletedFalse(String templateCode, UUID branchId);

    boolean existsByTemplateCodeAndBranchIdAndIsDeletedFalse(String templateCode, UUID branchId);

    Page<NotificationTemplate> findAllByBranchIdAndIsDeletedFalseAndTemplateNameContainingIgnoreCase(UUID branchId, String templateName, Pageable pageable);
}
