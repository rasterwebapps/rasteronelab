package com.rasteronelab.lis.result.application.service;

import com.rasteronelab.lis.core.common.exception.BusinessRuleException;
import com.rasteronelab.lis.core.common.exception.NotFoundException;
import com.rasteronelab.lis.core.infrastructure.BranchContextHolder;
import com.rasteronelab.lis.result.api.dto.ResultEntryRequest;
import com.rasteronelab.lis.result.api.dto.TestResultCreateRequest;
import com.rasteronelab.lis.result.api.dto.TestResultResponse;
import com.rasteronelab.lis.result.api.mapper.TestResultMapper;
import com.rasteronelab.lis.result.domain.model.AbnormalFlag;
import com.rasteronelab.lis.result.domain.model.ResultHistory;
import com.rasteronelab.lis.result.domain.model.ResultStatus;
import com.rasteronelab.lis.result.domain.model.ResultType;
import com.rasteronelab.lis.result.domain.model.ResultValue;
import com.rasteronelab.lis.result.domain.model.TestResult;
import com.rasteronelab.lis.result.domain.repository.ResultHistoryRepository;
import com.rasteronelab.lis.result.domain.repository.ResultValueRepository;
import com.rasteronelab.lis.result.domain.repository.TestResultRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Service for test result CRUD and result entry operations.
 * Handles creating results, entering values, abnormal flag calculation, and validation.
 */
@Service
@Transactional
public class ResultEntryService {

    private final TestResultRepository testResultRepository;
    private final ResultValueRepository resultValueRepository;
    private final ResultHistoryRepository resultHistoryRepository;
    private final TestResultMapper testResultMapper;

    public ResultEntryService(TestResultRepository testResultRepository,
                              ResultValueRepository resultValueRepository,
                              ResultHistoryRepository resultHistoryRepository,
                              TestResultMapper testResultMapper) {
        this.testResultRepository = testResultRepository;
        this.resultValueRepository = resultValueRepository;
        this.resultHistoryRepository = resultHistoryRepository;
        this.testResultMapper = testResultMapper;
    }

    public TestResultResponse createTestResult(TestResultCreateRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        TestResult testResult = testResultMapper.toEntity(request);
        testResult.setBranchId(branchId);
        testResult.setStatus(ResultStatus.PENDING);
        testResult.setIsActive(true);

        TestResult saved = testResultRepository.save(testResult);
        recordHistory(saved, null, ResultStatus.PENDING, "CREATED", "Test result created");
        return testResultMapper.toResponse(saved);
    }

    public TestResultResponse enterResults(ResultEntryRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        String currentUser = getCurrentUser();

        TestResult testResult = testResultRepository
                .findByIdAndBranchIdAndIsDeletedFalse(request.getTestResultId(), branchId)
                .orElseThrow(() -> new NotFoundException("TestResult", request.getTestResultId()));

        if (testResult.getStatus() != ResultStatus.PENDING && testResult.getStatus() != ResultStatus.ENTERED) {
            throw new BusinessRuleException("Cannot enter results for a test result in status: " + testResult.getStatus());
        }

        if (request.getValues() != null) {
            // Clear existing values for re-entry
            List<ResultValue> existingValues = resultValueRepository
                    .findAllByTestResultIdAndBranchIdAndIsDeletedFalse(testResult.getId(), branchId);
            for (ResultValue existing : existingValues) {
                existing.softDelete(currentUser);
                resultValueRepository.save(existing);
            }

            boolean hasCriticalValue = false;

            for (ResultEntryRequest.ResultValueEntry entry : request.getValues()) {
                ResultValue resultValue = buildResultValue(entry, branchId, testResult);

                // Calculate abnormal flag for numeric values with reference ranges
                if (resultValue.getNumericValue() != null
                        && resultValue.getReferenceRangeLow() != null
                        && resultValue.getReferenceRangeHigh() != null) {
                    AbnormalFlag flag = calculateAbnormalFlag(
                            resultValue.getNumericValue(),
                            resultValue.getReferenceRangeLow(),
                            resultValue.getReferenceRangeHigh());
                    resultValue.setAbnormalFlag(flag);
                    if (flag == AbnormalFlag.CRITICAL_LOW || flag == AbnormalFlag.CRITICAL_HIGH) {
                        resultValue.setIsCritical(true);
                        hasCriticalValue = true;
                    }
                }

                resultValueRepository.save(resultValue);
            }

            testResult.setIsCritical(hasCriticalValue);
        }

        if (request.getComments() != null) {
            testResult.setComments(request.getComments());
        }

        ResultStatus previousStatus = testResult.getStatus();
        testResult.setStatus(ResultStatus.ENTERED);
        testResult.setEnteredBy(currentUser);
        testResult.setEnteredAt(LocalDateTime.now());

        TestResult saved = testResultRepository.save(testResult);
        recordHistory(saved, previousStatus, ResultStatus.ENTERED, "RESULT_ENTERED", "Results entered by " + currentUser);
        return testResultMapper.toResponse(saved);
    }

    public TestResultResponse validateResult(UUID testResultId) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        String currentUser = getCurrentUser();

        TestResult testResult = testResultRepository
                .findByIdAndBranchIdAndIsDeletedFalse(testResultId, branchId)
                .orElseThrow(() -> new NotFoundException("TestResult", testResultId));

        if (testResult.getStatus() != ResultStatus.ENTERED) {
            throw new BusinessRuleException("Only results in ENTERED status can be validated. Current status: " + testResult.getStatus());
        }

        ResultStatus previousStatus = testResult.getStatus();
        testResult.setStatus(ResultStatus.VALIDATED);
        testResult.setValidatedBy(currentUser);
        testResult.setValidatedAt(LocalDateTime.now());

        TestResult saved = testResultRepository.save(testResult);
        recordHistory(saved, previousStatus, ResultStatus.VALIDATED, "VALIDATED", "Result validated by " + currentUser);
        return testResultMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public TestResultResponse getById(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        TestResult testResult = testResultRepository
                .findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("TestResult", id));
        return testResultMapper.toResponse(testResult);
    }

    @Transactional(readOnly = true)
    public Page<TestResultResponse> getByOrder(UUID orderId, Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return testResultRepository
                .findAllByOrderIdAndBranchIdAndIsDeletedFalse(orderId, branchId, pageable)
                .map(testResultMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<TestResultResponse> getByPatient(UUID patientId, Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return testResultRepository
                .findAllByPatientIdAndBranchIdAndIsDeletedFalse(patientId, branchId, pageable)
                .map(testResultMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<TestResultResponse> getWorklist(UUID departmentId, Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return testResultRepository
                .findAllByDepartmentIdAndStatusAndBranchIdAndIsDeletedFalse(
                        departmentId, ResultStatus.PENDING, branchId, pageable)
                .map(testResultMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<TestResultResponse> getAll(Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return testResultRepository
                .findAllByBranchIdAndIsDeletedFalse(branchId, pageable)
                .map(testResultMapper::toResponse);
    }

    private AbnormalFlag calculateAbnormalFlag(BigDecimal value, BigDecimal low, BigDecimal high) {
        BigDecimal criticalLowThreshold = low.subtract(low.multiply(new BigDecimal("0.5")));
        BigDecimal criticalHighThreshold = high.add(high.multiply(new BigDecimal("0.5")));

        if (value.compareTo(criticalLowThreshold) < 0) {
            return AbnormalFlag.CRITICAL_LOW;
        } else if (value.compareTo(low) < 0) {
            return AbnormalFlag.LOW;
        } else if (value.compareTo(criticalHighThreshold) > 0) {
            return AbnormalFlag.CRITICAL_HIGH;
        } else if (value.compareTo(high) > 0) {
            return AbnormalFlag.HIGH;
        }
        return AbnormalFlag.NORMAL;
    }

    private ResultType parseResultType(String resultType) {
        if (resultType == null || resultType.isBlank()) {
            return ResultType.NUMERIC;
        }
        try {
            return ResultType.valueOf(resultType.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ResultType.NUMERIC;
        }
    }

    /**
     * Builds a ResultValue entity from a request entry, populating all fields
     * including reference ranges from the request.
     */
    static ResultValue buildResultValue(ResultEntryRequest.ResultValueEntry entry,
                                        UUID branchId, TestResult testResult) {
        ResultValue resultValue = new ResultValue();
        resultValue.setBranchId(branchId);
        resultValue.setTestResult(testResult);
        resultValue.setParameterId(entry.getParameterId());
        resultValue.setParameterCode(entry.getParameterCode());
        resultValue.setParameterName(entry.getParameterName());
        resultValue.setUnit(entry.getUnit());

        ResultType resultType;
        if (entry.getResultType() == null || entry.getResultType().isBlank()) {
            resultType = ResultType.NUMERIC;
        } else {
            try {
                resultType = ResultType.valueOf(entry.getResultType().toUpperCase());
            } catch (IllegalArgumentException e) {
                resultType = ResultType.NUMERIC;
            }
        }
        resultValue.setResultType(resultType);

        if (entry.getNumericValue() != null && !entry.getNumericValue().isBlank()) {
            resultValue.setNumericValue(new BigDecimal(entry.getNumericValue()));
        }
        resultValue.setTextValue(entry.getTextValue());

        if (entry.getReferenceRangeLow() != null && !entry.getReferenceRangeLow().isBlank()) {
            resultValue.setReferenceRangeLow(new BigDecimal(entry.getReferenceRangeLow()));
        }
        if (entry.getReferenceRangeHigh() != null && !entry.getReferenceRangeHigh().isBlank()) {
            resultValue.setReferenceRangeHigh(new BigDecimal(entry.getReferenceRangeHigh()));
        }
        resultValue.setReferenceRangeText(entry.getReferenceRangeText());

        return resultValue;
    }

    private void recordHistory(TestResult testResult, ResultStatus previousStatus,
                               ResultStatus newStatus, String action, String details) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        String currentUser = getCurrentUser();

        ResultHistory history = new ResultHistory();
        history.setBranchId(branchId);
        history.setTestResultId(testResult.getId());
        history.setAction(action);
        history.setPreviousStatus(previousStatus != null ? previousStatus.name() : null);
        history.setNewStatus(newStatus.name());
        history.setPerformedBy(currentUser);
        history.setPerformedAt(LocalDateTime.now());
        history.setDetails(details);

        resultHistoryRepository.save(history);
    }

    private String getCurrentUser() {
        return SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : "system";
    }
}
