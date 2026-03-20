package com.rasteronelab.lis.result.application.service;

import com.rasteronelab.lis.core.common.exception.BusinessRuleException;
import com.rasteronelab.lis.core.common.exception.NotFoundException;
import com.rasteronelab.lis.core.infrastructure.BranchContextHolder;
import com.rasteronelab.lis.result.api.dto.ResultEntryRequest;
import com.rasteronelab.lis.result.api.dto.TestResultCreateRequest;
import com.rasteronelab.lis.result.api.dto.TestResultResponse;
import com.rasteronelab.lis.result.api.mapper.TestResultMapper;
import com.rasteronelab.lis.result.domain.model.AbnormalFlag;
import com.rasteronelab.lis.result.domain.model.ResultStatus;
import com.rasteronelab.lis.result.domain.model.ResultValue;
import com.rasteronelab.lis.result.domain.model.TestResult;
import com.rasteronelab.lis.result.domain.repository.ResultHistoryRepository;
import com.rasteronelab.lis.result.domain.repository.ResultValueRepository;
import com.rasteronelab.lis.result.domain.repository.TestResultRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("ResultEntryService")
@ExtendWith(MockitoExtension.class)
class ResultEntryServiceTest {

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
    private ResultEntryService resultEntryService;

    @BeforeEach
    void setUp() {
        BranchContextHolder.setCurrentBranchId(BRANCH_ID);
        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken("lab_tech", null));
    }

    @AfterEach
    void tearDown() {
        BranchContextHolder.clear();
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("createTestResult should save result in PENDING status")
    void createTestResult_shouldSaveInPendingStatus() {
        TestResultCreateRequest request = new TestResultCreateRequest();
        request.setOrderId(UUID.randomUUID());
        request.setOrderLineItemId(UUID.randomUUID());
        request.setPatientId(UUID.randomUUID());
        request.setTestId(UUID.randomUUID());
        request.setTestCode("CBC");
        request.setTestName("Complete Blood Count");

        TestResult entity = new TestResult();
        TestResult saved = new TestResult();
        saved.setId(UUID.randomUUID());
        TestResultResponse response = new TestResultResponse();

        when(testResultMapper.toEntity(request)).thenReturn(entity);
        when(testResultRepository.save(entity)).thenReturn(saved);
        when(testResultMapper.toResponse(saved)).thenReturn(response);

        TestResultResponse result = resultEntryService.createTestResult(request);

        assertThat(result).isEqualTo(response);
        assertThat(entity.getStatus()).isEqualTo(ResultStatus.PENDING);
        assertThat(entity.getBranchId()).isEqualTo(BRANCH_ID);
        verify(testResultRepository).save(entity);
        verify(resultHistoryRepository).save(any());
    }

    @Test
    @DisplayName("enterResults should transition to ENTERED and set enteredBy/enteredAt")
    void enterResults_shouldTransitionToEntered() {
        UUID testResultId = UUID.randomUUID();
        TestResult testResult = new TestResult();
        testResult.setId(testResultId);
        testResult.setStatus(ResultStatus.PENDING);
        testResult.setBranchId(BRANCH_ID);
        TestResult saved = new TestResult();
        TestResultResponse response = new TestResultResponse();

        ResultEntryRequest request = new ResultEntryRequest();
        request.setTestResultId(testResultId);
        request.setValues(Collections.emptyList());
        request.setComments("All values normal");

        when(testResultRepository.findByIdAndBranchIdAndIsDeletedFalse(testResultId, BRANCH_ID))
                .thenReturn(Optional.of(testResult));
        when(resultValueRepository.findAllByTestResultIdAndBranchIdAndIsDeletedFalse(testResultId, BRANCH_ID))
                .thenReturn(Collections.emptyList());
        when(testResultRepository.save(testResult)).thenReturn(saved);
        when(testResultMapper.toResponse(saved)).thenReturn(response);

        TestResultResponse result = resultEntryService.enterResults(request);

        assertThat(result).isEqualTo(response);
        assertThat(testResult.getStatus()).isEqualTo(ResultStatus.ENTERED);
        assertThat(testResult.getEnteredBy()).isEqualTo("lab_tech");
        assertThat(testResult.getEnteredAt()).isNotNull();
        assertThat(testResult.getComments()).isEqualTo("All values normal");
    }

    @Test
    @DisplayName("enterResults should reject if status is not PENDING or ENTERED")
    void enterResults_shouldRejectInvalidStatus() {
        UUID testResultId = UUID.randomUUID();
        TestResult testResult = new TestResult();
        testResult.setId(testResultId);
        testResult.setStatus(ResultStatus.AUTHORIZED);

        ResultEntryRequest request = new ResultEntryRequest();
        request.setTestResultId(testResultId);

        when(testResultRepository.findByIdAndBranchIdAndIsDeletedFalse(testResultId, BRANCH_ID))
                .thenReturn(Optional.of(testResult));

        assertThatThrownBy(() -> resultEntryService.enterResults(request))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("AUTHORIZED");
    }

    @Test
    @DisplayName("enterResults with numeric value should calculate abnormal flag")
    void enterResults_shouldCalculateAbnormalFlag() {
        UUID testResultId = UUID.randomUUID();
        TestResult testResult = new TestResult();
        testResult.setId(testResultId);
        testResult.setStatus(ResultStatus.PENDING);
        testResult.setBranchId(BRANCH_ID);
        TestResult saved = new TestResult();
        TestResultResponse response = new TestResultResponse();

        ResultEntryRequest.ResultValueEntry valueEntry = new ResultEntryRequest.ResultValueEntry();
        valueEntry.setParameterId(UUID.randomUUID());
        valueEntry.setParameterCode("GLU");
        valueEntry.setParameterName("Glucose");
        valueEntry.setResultType("NUMERIC");
        valueEntry.setNumericValue("250.0");
        valueEntry.setUnit("mg/dL");
        valueEntry.setReferenceRangeLow("70.0");
        valueEntry.setReferenceRangeHigh("100.0");

        ResultEntryRequest request = new ResultEntryRequest();
        request.setTestResultId(testResultId);
        request.setValues(List.of(valueEntry));

        when(testResultRepository.findByIdAndBranchIdAndIsDeletedFalse(testResultId, BRANCH_ID))
                .thenReturn(Optional.of(testResult));
        when(resultValueRepository.findAllByTestResultIdAndBranchIdAndIsDeletedFalse(testResultId, BRANCH_ID))
                .thenReturn(Collections.emptyList());
        when(testResultRepository.save(testResult)).thenReturn(saved);
        when(testResultMapper.toResponse(saved)).thenReturn(response);

        resultEntryService.enterResults(request);

        ArgumentCaptor<ResultValue> captor = ArgumentCaptor.forClass(ResultValue.class);
        verify(resultValueRepository).save(captor.capture());
        ResultValue savedValue = captor.getValue();

        // 250 > 100 * 1.5 = 150 → CRITICAL_HIGH
        assertThat(savedValue.getAbnormalFlag()).isEqualTo(AbnormalFlag.CRITICAL_HIGH);
        assertThat(savedValue.getIsCritical()).isTrue();
        assertThat(testResult.getIsCritical()).isTrue();
    }

    @Test
    @DisplayName("enterResults with value in normal range should have NORMAL flag")
    void enterResults_normalValue_shouldHaveNormalFlag() {
        UUID testResultId = UUID.randomUUID();
        TestResult testResult = new TestResult();
        testResult.setId(testResultId);
        testResult.setStatus(ResultStatus.PENDING);
        testResult.setBranchId(BRANCH_ID);
        TestResult saved = new TestResult();
        TestResultResponse response = new TestResultResponse();

        ResultEntryRequest.ResultValueEntry valueEntry = new ResultEntryRequest.ResultValueEntry();
        valueEntry.setParameterId(UUID.randomUUID());
        valueEntry.setParameterCode("GLU");
        valueEntry.setParameterName("Glucose");
        valueEntry.setNumericValue("85.0");
        valueEntry.setReferenceRangeLow("70.0");
        valueEntry.setReferenceRangeHigh("100.0");

        ResultEntryRequest request = new ResultEntryRequest();
        request.setTestResultId(testResultId);
        request.setValues(List.of(valueEntry));

        when(testResultRepository.findByIdAndBranchIdAndIsDeletedFalse(testResultId, BRANCH_ID))
                .thenReturn(Optional.of(testResult));
        when(resultValueRepository.findAllByTestResultIdAndBranchIdAndIsDeletedFalse(testResultId, BRANCH_ID))
                .thenReturn(Collections.emptyList());
        when(testResultRepository.save(testResult)).thenReturn(saved);
        when(testResultMapper.toResponse(saved)).thenReturn(response);

        resultEntryService.enterResults(request);

        ArgumentCaptor<ResultValue> captor = ArgumentCaptor.forClass(ResultValue.class);
        verify(resultValueRepository).save(captor.capture());
        ResultValue savedValue = captor.getValue();

        assertThat(savedValue.getAbnormalFlag()).isEqualTo(AbnormalFlag.NORMAL);
        assertThat(testResult.getIsCritical()).isFalse();
    }

    @Test
    @DisplayName("validateResult should transition from ENTERED to VALIDATED")
    void validateResult_shouldTransitionToValidated() {
        UUID testResultId = UUID.randomUUID();
        TestResult testResult = new TestResult();
        testResult.setId(testResultId);
        testResult.setStatus(ResultStatus.ENTERED);
        TestResult saved = new TestResult();
        TestResultResponse response = new TestResultResponse();

        when(testResultRepository.findByIdAndBranchIdAndIsDeletedFalse(testResultId, BRANCH_ID))
                .thenReturn(Optional.of(testResult));
        when(testResultRepository.save(testResult)).thenReturn(saved);
        when(testResultMapper.toResponse(saved)).thenReturn(response);

        TestResultResponse result = resultEntryService.validateResult(testResultId);

        assertThat(result).isEqualTo(response);
        assertThat(testResult.getStatus()).isEqualTo(ResultStatus.VALIDATED);
        assertThat(testResult.getValidatedBy()).isEqualTo("lab_tech");
        assertThat(testResult.getValidatedAt()).isNotNull();
    }

    @Test
    @DisplayName("validateResult should reject if status is not ENTERED")
    void validateResult_invalidStatus_shouldThrow() {
        UUID testResultId = UUID.randomUUID();
        TestResult testResult = new TestResult();
        testResult.setId(testResultId);
        testResult.setStatus(ResultStatus.PENDING);

        when(testResultRepository.findByIdAndBranchIdAndIsDeletedFalse(testResultId, BRANCH_ID))
                .thenReturn(Optional.of(testResult));

        assertThatThrownBy(() -> resultEntryService.validateResult(testResultId))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("ENTERED");
    }

    @Test
    @DisplayName("getById should return result when found")
    void getById_shouldReturnResult() {
        UUID id = UUID.randomUUID();
        TestResult testResult = new TestResult();
        TestResultResponse response = new TestResultResponse();

        when(testResultRepository.findByIdAndBranchIdAndIsDeletedFalse(id, BRANCH_ID))
                .thenReturn(Optional.of(testResult));
        when(testResultMapper.toResponse(testResult)).thenReturn(response);

        TestResultResponse result = resultEntryService.getById(id);

        assertThat(result).isEqualTo(response);
    }

    @Test
    @DisplayName("getById should throw when not found")
    void getById_notFound_shouldThrow() {
        UUID id = UUID.randomUUID();

        when(testResultRepository.findByIdAndBranchIdAndIsDeletedFalse(id, BRANCH_ID))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> resultEntryService.getById(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("TestResult");
    }
}
