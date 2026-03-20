package com.rasteronelab.lis.result.domain.repository;

import com.rasteronelab.lis.core.domain.repository.BranchAwareRepository;
import com.rasteronelab.lis.result.domain.model.ResultStatus;
import com.rasteronelab.lis.result.domain.model.TestResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface TestResultRepository extends BranchAwareRepository<TestResult> {

    Page<TestResult> findAllByOrderIdAndBranchIdAndIsDeletedFalse(UUID orderId, UUID branchId, Pageable pageable);

    Page<TestResult> findAllByPatientIdAndBranchIdAndIsDeletedFalse(UUID patientId, UUID branchId, Pageable pageable);

    Page<TestResult> findAllByStatusAndBranchIdAndIsDeletedFalse(ResultStatus status, UUID branchId, Pageable pageable);

    Page<TestResult> findAllByDepartmentIdAndStatusAndBranchIdAndIsDeletedFalse(
            UUID departmentId, ResultStatus status, UUID branchId, Pageable pageable);

    List<TestResult> findAllByOrderIdAndBranchIdAndIsDeletedFalse(UUID orderId, UUID branchId);

    List<TestResult> findAllByPatientIdAndTestIdAndBranchIdAndIsDeletedFalseOrderByEnteredAtDesc(
            UUID patientId, UUID testId, UUID branchId);
}
