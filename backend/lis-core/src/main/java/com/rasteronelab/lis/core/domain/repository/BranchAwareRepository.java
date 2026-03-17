package com.rasteronelab.lis.core.domain.repository;

import com.rasteronelab.lis.core.domain.model.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Base repository for all branch-scoped entities.
 * Provides common query methods that enforce branch isolation and soft-delete filtering.
 *
 * USAGE:
 * All module-specific repositories should extend this interface:
 * {@code public interface PatientRepository extends BranchAwareRepository<Patient> { }}
 *
 * RULES:
 * - Always pass branchId from BranchContextHolder.getCurrentBranchId()
 * - Never use JpaRepository methods directly (they bypass branch filtering)
 */
@NoRepositoryBean
public interface BranchAwareRepository<T extends BaseEntity> extends JpaRepository<T, UUID> {

    Optional<T> findByIdAndBranchIdAndIsDeletedFalse(UUID id, UUID branchId);

    List<T> findAllByBranchIdAndIsDeletedFalse(UUID branchId);

    Page<T> findAllByBranchIdAndIsDeletedFalse(UUID branchId, Pageable pageable);

    boolean existsByIdAndBranchIdAndIsDeletedFalse(UUID id, UUID branchId);

    long countByBranchIdAndIsDeletedFalse(UUID branchId);
}
