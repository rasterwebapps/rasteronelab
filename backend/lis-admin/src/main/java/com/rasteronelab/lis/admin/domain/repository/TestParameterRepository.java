package com.rasteronelab.lis.admin.domain.repository;

import com.rasteronelab.lis.admin.domain.model.TestParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository for TestParameter mapping entity.
 * Queries for test-parameter associations with soft-delete filtering.
 */
@Repository
public interface TestParameterRepository extends JpaRepository<TestParameter, UUID> {

    List<TestParameter> findAllByTestIdAndIsDeletedFalse(UUID testId);

    boolean existsByTestIdAndParameterIdAndIsDeletedFalse(UUID testId, UUID parameterId);
}
