package com.rasteronelab.lis.admin.domain.repository;

import com.rasteronelab.lis.admin.domain.model.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository for Department entity.
 * All queries filter by isDeleted=false to enforce soft-delete semantics.
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, UUID> {

    Optional<Department> findByIdAndIsDeletedFalse(UUID id);

    Page<Department> findAllByOrgIdAndIsDeletedFalse(UUID orgId, Pageable pageable);

    Optional<Department> findByCodeAndOrgIdAndIsDeletedFalse(String code, UUID orgId);

    boolean existsByCodeAndOrgIdAndIsDeletedFalse(String code, UUID orgId);

    List<Department> findAllByOrgIdAndIsDeletedFalseOrderByDisplayOrderAsc(UUID orgId);
}
