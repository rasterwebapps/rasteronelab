package com.rasteronelab.lis.admin.domain.repository;

import com.rasteronelab.lis.admin.domain.model.Branch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository for Branch entity.
 * All queries filter by isDeleted=false to enforce soft-delete semantics.
 */
@Repository
public interface BranchRepository extends JpaRepository<Branch, UUID> {

    Optional<Branch> findByIdAndIsDeletedFalse(UUID id);

    Page<Branch> findAllByIsDeletedFalse(Pageable pageable);

    Page<Branch> findAllByOrgIdAndIsDeletedFalse(UUID orgId, Pageable pageable);

    Optional<Branch> findByCodeAndOrgIdAndIsDeletedFalse(String code, UUID orgId);

    boolean existsByCodeAndOrgIdAndIsDeletedFalse(String code, UUID orgId);
}
