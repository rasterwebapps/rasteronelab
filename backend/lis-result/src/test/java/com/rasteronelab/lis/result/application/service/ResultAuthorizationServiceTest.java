package com.rasteronelab.lis.result.application.service;

import com.rasteronelab.lis.core.common.exception.BusinessRuleException;
import com.rasteronelab.lis.core.common.exception.NotFoundException;
import com.rasteronelab.lis.core.infrastructure.BranchContextHolder;
import com.rasteronelab.lis.result.api.dto.ResultAmendRequest;
import com.rasteronelab.lis.result.api.dto.TestResultResponse;
import com.rasteronelab.lis.result.api.mapper.TestResultMapper;
import com.rasteronelab.lis.result.domain.model.ResultStatus;
import com.rasteronelab.lis.result.domain.model.TestResult;
import com.rasteronelab.lis.result.domain.repository.ResultHistoryRepository;
import com.rasteronelab.lis.result.domain.repository.ResultValueRepository;
import com.rasteronelab.lis.result.domain.repository.TestResultRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("ResultAuthorizationService")
@ExtendWith(MockitoExtension.class)
class ResultAuthorizationServiceTest {

    private static final UUID BRANCH_ID = UUID.randomUUID();

    @Mock
    private TestResultRepository testResultRepository;

    @Mock
    private ResultValueRepository resultValueRepository;

    @Mock
    private ResultHistoryRepository resultHistoryRepository;

    @Mock
    private TestResultMapper testResultMapper;

    @InjectMocks
    private ResultAuthorizationService resultAuthorizationService;

    @BeforeEach
    void setUp() {
        BranchContextHolder.setCurrentBranchId(BRANCH_ID);
        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken("pathologist", null));
    }

    @AfterEach
    void tearDown() {
        BranchContextHolder.clear();
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("authorizeResult should transition VALIDATED to AUTHORIZED")
    void authorizeResult_shouldTransitionToAuthorized() {
        UUID testResultId = UUID.randomUUID();
        TestResult testResult = new TestResult();
        testResult.setId(testResultId);
        testResult.setStatus(ResultStatus.VALIDATED);
        TestResult saved = new TestResult();
        TestResultResponse response = new TestResultResponse();

        when(testResultRepository.findByIdAndBranchIdAndIsDeletedFalse(testResultId, BRANCH_ID))
                .thenReturn(Optional.of(testResult));
        when(testResultRepository.save(testResult)).thenReturn(saved);
        when(testResultMapper.toResponse(saved)).thenReturn(response);

        TestResultResponse result = resultAuthorizationService.authorizeResult(testResultId);

        assertThat(result).isEqualTo(response);
        assertThat(testResult.getStatus()).isEqualTo(ResultStatus.AUTHORIZED);
        assertThat(testResult.getAuthorizedBy()).isEqualTo("pathologist");
        assertThat(testResult.getAuthorizedAt()).isNotNull();
        verify(resultHistoryRepository).save(any());
    }

    @Test
    @DisplayName("authorizeResult should reject non-VALIDATED status")
    void authorizeResult_invalidStatus_shouldThrow() {
        UUID testResultId = UUID.randomUUID();
        TestResult testResult = new TestResult();
        testResult.setId(testResultId);
        testResult.setStatus(ResultStatus.ENTERED);

        when(testResultRepository.findByIdAndBranchIdAndIsDeletedFalse(testResultId, BRANCH_ID))
                .thenReturn(Optional.of(testResult));

        assertThatThrownBy(() -> resultAuthorizationService.authorizeResult(testResultId))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("VALIDATED");
    }

    @Test
    @DisplayName("authorizeResult not found should throw NotFoundException")
    void authorizeResult_notFound_shouldThrow() {
        UUID testResultId = UUID.randomUUID();

        when(testResultRepository.findByIdAndBranchIdAndIsDeletedFalse(testResultId, BRANCH_ID))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> resultAuthorizationService.authorizeResult(testResultId))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("batchAuthorize should authorize multiple results")
    void batchAuthorize_shouldAuthorizeMultiple() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();

        TestResult result1 = new TestResult();
        result1.setId(id1);
        result1.setStatus(ResultStatus.VALIDATED);
        TestResult saved1 = new TestResult();
        TestResultResponse response1 = new TestResultResponse();

        TestResult result2 = new TestResult();
        result2.setId(id2);
        result2.setStatus(ResultStatus.VALIDATED);
        TestResult saved2 = new TestResult();
        TestResultResponse response2 = new TestResultResponse();

        when(testResultRepository.findByIdAndBranchIdAndIsDeletedFalse(id1, BRANCH_ID))
                .thenReturn(Optional.of(result1));
        when(testResultRepository.findByIdAndBranchIdAndIsDeletedFalse(id2, BRANCH_ID))
                .thenReturn(Optional.of(result2));
        when(testResultRepository.save(result1)).thenReturn(saved1);
        when(testResultRepository.save(result2)).thenReturn(saved2);
        when(testResultMapper.toResponse(saved1)).thenReturn(response1);
        when(testResultMapper.toResponse(saved2)).thenReturn(response2);

        List<TestResultResponse> results = resultAuthorizationService.batchAuthorize(List.of(id1, id2));

        assertThat(results).hasSize(2);
        assertThat(result1.getStatus()).isEqualTo(ResultStatus.AUTHORIZED);
        assertThat(result2.getStatus()).isEqualTo(ResultStatus.AUTHORIZED);
    }

    @Test
    @DisplayName("amendResult should transition AUTHORIZED to AMENDED")
    void amendResult_shouldTransitionToAmended() {
        UUID testResultId = UUID.randomUUID();
        TestResult testResult = new TestResult();
        testResult.setId(testResultId);
        testResult.setStatus(ResultStatus.AUTHORIZED);
        testResult.setBranchId(BRANCH_ID);
        TestResult saved = new TestResult();
        TestResultResponse response = new TestResultResponse();

        ResultAmendRequest request = new ResultAmendRequest();
        request.setTestResultId(testResultId);
        request.setAmendmentReason("Incorrect value entered");

        when(testResultRepository.findByIdAndBranchIdAndIsDeletedFalse(testResultId, BRANCH_ID))
                .thenReturn(Optional.of(testResult));
        when(testResultRepository.save(testResult)).thenReturn(saved);
        when(testResultMapper.toResponse(saved)).thenReturn(response);

        TestResultResponse result = resultAuthorizationService.amendResult(request);

        assertThat(result).isEqualTo(response);
        assertThat(testResult.getStatus()).isEqualTo(ResultStatus.AMENDED);
        assertThat(testResult.getIsAmended()).isTrue();
        assertThat(testResult.getAmendmentReason()).isEqualTo("Incorrect value entered");
        assertThat(testResult.getAmendedBy()).isEqualTo("pathologist");
        assertThat(testResult.getAmendedAt()).isNotNull();
    }

    @Test
    @DisplayName("amendResult should reject non-AUTHORIZED/RELEASED status")
    void amendResult_invalidStatus_shouldThrow() {
        UUID testResultId = UUID.randomUUID();
        TestResult testResult = new TestResult();
        testResult.setId(testResultId);
        testResult.setStatus(ResultStatus.ENTERED);

        ResultAmendRequest request = new ResultAmendRequest();
        request.setTestResultId(testResultId);
        request.setAmendmentReason("Test");

        when(testResultRepository.findByIdAndBranchIdAndIsDeletedFalse(testResultId, BRANCH_ID))
                .thenReturn(Optional.of(testResult));

        assertThatThrownBy(() -> resultAuthorizationService.amendResult(request))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("AUTHORIZED or RELEASED");
    }
}
