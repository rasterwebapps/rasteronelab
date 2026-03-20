package com.rasteronelab.lis.result.application.service;

import com.rasteronelab.lis.core.common.exception.BusinessRuleException;
import com.rasteronelab.lis.core.common.exception.NotFoundException;
import com.rasteronelab.lis.core.infrastructure.BranchContextHolder;
import com.rasteronelab.lis.result.api.dto.ResultAmendRequest;
import com.rasteronelab.lis.result.api.dto.ResultEntryRequest;
import com.rasteronelab.lis.result.api.dto.TestResultResponse;
import com.rasteronelab.lis.result.api.mapper.TestResultMapper;
import com.rasteronelab.lis.result.domain.model.ResultHistory;
import com.rasteronelab.lis.result.domain.model.ResultStatus;
import com.rasteronelab.lis.result.domain.model.ResultValue;
import com.rasteronelab.lis.result.domain.model.TestResult;
import com.rasteronelab.lis.result.domain.repository.ResultHistoryRepository;
import com.rasteronelab.lis.result.domain.repository.ResultValueRepository;
import com.rasteronelab.lis.result.domain.repository.TestResultRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Service for result authorization and amendment workflows.
 * Handles VALIDATED → AUTHORIZED transition and amendment of authorized results.
 */
@Service
@Transactional
public class ResultAuthorizationService {

    private final TestResultRepository testResultRepository;
    private final ResultValueRepository resultValueRepository;
    private final ResultHistoryRepository resultHistoryRepository;
    private final TestResultMapper testResultMapper;

    public ResultAuthorizationService(TestResultRepository testResultRepository,
                                      ResultValueRepository resultValueRepository,
                                      ResultHistoryRepository resultHistoryRepository,
                                      TestResultMapper testResultMapper) {
        this.testResultRepository = testResultRepository;
        this.resultValueRepository = resultValueRepository;
        this.resultHistoryRepository = resultHistoryRepository;
        this.testResultMapper = testResultMapper;
    }

    public TestResultResponse authorizeResult(UUID testResultId) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        String currentUser = getCurrentUser();

        TestResult testResult = testResultRepository
                .findByIdAndBranchIdAndIsDeletedFalse(testResultId, branchId)
                .orElseThrow(() -> new NotFoundException("TestResult", testResultId));

        if (testResult.getStatus() != ResultStatus.VALIDATED) {
            throw new BusinessRuleException("Only results in VALIDATED status can be authorized. Current status: " + testResult.getStatus());
        }

        ResultStatus previousStatus = testResult.getStatus();
        testResult.setStatus(ResultStatus.AUTHORIZED);
        testResult.setAuthorizedBy(currentUser);
        testResult.setAuthorizedAt(LocalDateTime.now());

        TestResult saved = testResultRepository.save(testResult);
        recordHistory(saved, previousStatus, ResultStatus.AUTHORIZED, "AUTHORIZED", "Result authorized by " + currentUser);
        return testResultMapper.toResponse(saved);
    }

    public List<TestResultResponse> batchAuthorize(List<UUID> resultIds) {
        List<TestResultResponse> responses = new ArrayList<>();
        for (UUID resultId : resultIds) {
            responses.add(authorizeResult(resultId));
        }
        return responses;
    }

    public TestResultResponse amendResult(ResultAmendRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        String currentUser = getCurrentUser();

        TestResult testResult = testResultRepository
                .findByIdAndBranchIdAndIsDeletedFalse(request.getTestResultId(), branchId)
                .orElseThrow(() -> new NotFoundException("TestResult", request.getTestResultId()));

        if (testResult.getStatus() != ResultStatus.AUTHORIZED && testResult.getStatus() != ResultStatus.RELEASED) {
            throw new BusinessRuleException(
                    "Only results in AUTHORIZED or RELEASED status can be amended. Current status: " + testResult.getStatus());
        }

        ResultStatus previousStatus = testResult.getStatus();
        testResult.setStatus(ResultStatus.AMENDED);
        testResult.setIsAmended(true);
        testResult.setAmendmentReason(request.getAmendmentReason());
        testResult.setAmendedBy(currentUser);
        testResult.setAmendedAt(LocalDateTime.now());

        // Update result values if provided
        if (request.getValues() != null && !request.getValues().isEmpty()) {
            List<ResultValue> existingValues = resultValueRepository
                    .findAllByTestResultIdAndBranchIdAndIsDeletedFalse(testResult.getId(), branchId);
            for (ResultValue existing : existingValues) {
                existing.softDelete(currentUser);
                resultValueRepository.save(existing);
            }

            for (ResultEntryRequest.ResultValueEntry entry : request.getValues()) {
                ResultValue resultValue = ResultEntryService.buildResultValue(entry, branchId, testResult);
                resultValueRepository.save(resultValue);
            }
        }

        TestResult saved = testResultRepository.save(testResult);
        recordHistory(saved, previousStatus, ResultStatus.AMENDED, "AMENDED",
                "Result amended by " + currentUser + ". Reason: " + request.getAmendmentReason());
        return testResultMapper.toResponse(saved);
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
