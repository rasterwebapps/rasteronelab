package com.rasteronelab.lis.admin.application.service;

import com.rasteronelab.lis.admin.api.dto.ReferenceRangeRequest;
import com.rasteronelab.lis.admin.api.dto.ReferenceRangeResponse;
import com.rasteronelab.lis.admin.api.mapper.ReferenceRangeMapper;
import com.rasteronelab.lis.admin.domain.model.Parameter;
import com.rasteronelab.lis.admin.domain.model.ReferenceRange;
import com.rasteronelab.lis.admin.domain.repository.ParameterRepository;
import com.rasteronelab.lis.admin.domain.repository.ReferenceRangeRepository;
import com.rasteronelab.lis.core.common.exception.BusinessRuleException;
import com.rasteronelab.lis.core.common.exception.NotFoundException;
import com.rasteronelab.lis.core.infrastructure.BranchContextHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("ReferenceRangeService")
@ExtendWith(MockitoExtension.class)
class ReferenceRangeServiceTest {

    private static final UUID BRANCH_ID = UUID.randomUUID();

    @Mock
    private ReferenceRangeRepository referenceRangeRepository;

    @Mock
    private ParameterRepository parameterRepository;

    @Mock
    private ReferenceRangeMapper referenceRangeMapper;

    @InjectMocks
    private ReferenceRangeService referenceRangeService;

    @BeforeEach
    void setUp() {
        BranchContextHolder.setCurrentBranchId(BRANCH_ID);
    }

    @AfterEach
    void tearDown() {
        BranchContextHolder.clear();
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("create should save and return reference range")
    void create_shouldSaveAndReturnReferenceRange() {
        UUID parameterId = UUID.randomUUID();
        ReferenceRangeRequest request = new ReferenceRangeRequest();
        request.setParameterId(parameterId);
        request.setAgeMin(BigDecimal.ZERO);
        request.setAgeMax(BigDecimal.TEN);
        request.setGender("M");
        Parameter parameter = new Parameter();
        ReferenceRange range = new ReferenceRange();
        ReferenceRange saved = new ReferenceRange();
        ReferenceRangeResponse response = new ReferenceRangeResponse();

        when(parameterRepository.findByIdAndBranchIdAndIsDeletedFalse(parameterId, BRANCH_ID)).thenReturn(Optional.of(parameter));
        when(referenceRangeRepository.findAllByParameterIdAndGenderAndBranchIdAndIsDeletedFalse(parameterId, "M", BRANCH_ID))
                .thenReturn(Collections.emptyList());
        when(referenceRangeMapper.toEntity(request)).thenReturn(range);
        when(referenceRangeRepository.save(range)).thenReturn(saved);
        when(referenceRangeMapper.toResponse(saved)).thenReturn(response);

        ReferenceRangeResponse result = referenceRangeService.create(request);

        assertThat(result).isEqualTo(response);
        assertThat(range.getBranchId()).isEqualTo(BRANCH_ID);
        verify(referenceRangeRepository).save(range);
    }

    @Test
    @DisplayName("create with parameter not found should throw NotFoundException")
    void create_withParameterNotFound_shouldThrowNotFoundException() {
        UUID parameterId = UUID.randomUUID();
        ReferenceRangeRequest request = new ReferenceRangeRequest();
        request.setParameterId(parameterId);

        when(parameterRepository.findByIdAndBranchIdAndIsDeletedFalse(parameterId, BRANCH_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> referenceRangeService.create(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Parameter");
    }

    @Test
    @DisplayName("create with overlapping age range should throw BusinessRuleException")
    void create_withOverlappingAgeRange_shouldThrowBusinessRuleException() {
        UUID parameterId = UUID.randomUUID();
        ReferenceRangeRequest request = new ReferenceRangeRequest();
        request.setParameterId(parameterId);
        request.setAgeMin(BigDecimal.valueOf(5));
        request.setAgeMax(BigDecimal.valueOf(15));
        request.setGender("M");
        Parameter parameter = new Parameter();

        ReferenceRange existing = new ReferenceRange();
        existing.setAgeMin(BigDecimal.ZERO);
        existing.setAgeMax(BigDecimal.TEN);

        when(parameterRepository.findByIdAndBranchIdAndIsDeletedFalse(parameterId, BRANCH_ID)).thenReturn(Optional.of(parameter));
        when(referenceRangeRepository.findAllByParameterIdAndGenderAndBranchIdAndIsDeletedFalse(parameterId, "M", BRANCH_ID))
                .thenReturn(List.of(existing));

        assertThatThrownBy(() -> referenceRangeService.create(request))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("overlap");
    }

    @Test
    @DisplayName("create with null age range should skip overlap validation")
    void create_withNullAgeRange_shouldSkipOverlapValidation() {
        UUID parameterId = UUID.randomUUID();
        ReferenceRangeRequest request = new ReferenceRangeRequest();
        request.setParameterId(parameterId);
        request.setAgeMin(null);
        request.setAgeMax(null);
        Parameter parameter = new Parameter();
        ReferenceRange range = new ReferenceRange();
        ReferenceRange saved = new ReferenceRange();
        ReferenceRangeResponse response = new ReferenceRangeResponse();

        when(parameterRepository.findByIdAndBranchIdAndIsDeletedFalse(parameterId, BRANCH_ID)).thenReturn(Optional.of(parameter));
        when(referenceRangeMapper.toEntity(request)).thenReturn(range);
        when(referenceRangeRepository.save(range)).thenReturn(saved);
        when(referenceRangeMapper.toResponse(saved)).thenReturn(response);

        ReferenceRangeResponse result = referenceRangeService.create(request);

        assertThat(result).isEqualTo(response);
    }

    @Test
    @DisplayName("create with null gender should query without gender filter")
    void create_withNullGender_shouldQueryWithoutGenderFilter() {
        UUID parameterId = UUID.randomUUID();
        ReferenceRangeRequest request = new ReferenceRangeRequest();
        request.setParameterId(parameterId);
        request.setAgeMin(BigDecimal.ZERO);
        request.setAgeMax(BigDecimal.TEN);
        request.setGender(null);
        Parameter parameter = new Parameter();
        ReferenceRange range = new ReferenceRange();
        ReferenceRange saved = new ReferenceRange();
        ReferenceRangeResponse response = new ReferenceRangeResponse();

        when(parameterRepository.findByIdAndBranchIdAndIsDeletedFalse(parameterId, BRANCH_ID)).thenReturn(Optional.of(parameter));
        when(referenceRangeRepository.findAllByParameterIdAndBranchIdAndIsDeletedFalse(parameterId, BRANCH_ID))
                .thenReturn(Collections.emptyList());
        when(referenceRangeMapper.toEntity(request)).thenReturn(range);
        when(referenceRangeRepository.save(range)).thenReturn(saved);
        when(referenceRangeMapper.toResponse(saved)).thenReturn(response);

        ReferenceRangeResponse result = referenceRangeService.create(request);

        assertThat(result).isEqualTo(response);
        verify(referenceRangeRepository).findAllByParameterIdAndBranchIdAndIsDeletedFalse(parameterId, BRANCH_ID);
    }

    @Test
    @DisplayName("getById should return reference range")
    void getById_shouldReturnReferenceRange() {
        UUID rangeId = UUID.randomUUID();
        ReferenceRange range = new ReferenceRange();
        ReferenceRangeResponse response = new ReferenceRangeResponse();

        when(referenceRangeRepository.findByIdAndBranchIdAndIsDeletedFalse(rangeId, BRANCH_ID)).thenReturn(Optional.of(range));
        when(referenceRangeMapper.toResponse(range)).thenReturn(response);

        ReferenceRangeResponse result = referenceRangeService.getById(rangeId);

        assertThat(result).isEqualTo(response);
    }

    @Test
    @DisplayName("getById not found should throw NotFoundException")
    void getById_notFound_shouldThrowNotFoundException() {
        UUID rangeId = UUID.randomUUID();

        when(referenceRangeRepository.findByIdAndBranchIdAndIsDeletedFalse(rangeId, BRANCH_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> referenceRangeService.getById(rangeId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("ReferenceRange");
    }

    @Test
    @DisplayName("getAll should return paged results")
    void getAll_shouldReturnPagedResults() {
        Pageable pageable = PageRequest.of(0, 10);
        ReferenceRange range = new ReferenceRange();
        ReferenceRangeResponse response = new ReferenceRangeResponse();
        Page<ReferenceRange> rangePage = new PageImpl<>(List.of(range));

        when(referenceRangeRepository.findAllByBranchIdAndIsDeletedFalse(BRANCH_ID, pageable)).thenReturn(rangePage);
        when(referenceRangeMapper.toResponse(range)).thenReturn(response);

        Page<ReferenceRangeResponse> result = referenceRangeService.getAll(pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst()).isEqualTo(response);
    }

    @Test
    @DisplayName("getByParameterId should return list of reference ranges")
    void getByParameterId_shouldReturnListOfReferenceRanges() {
        UUID parameterId = UUID.randomUUID();
        ReferenceRange range = new ReferenceRange();
        ReferenceRangeResponse response = new ReferenceRangeResponse();

        when(parameterRepository.existsByIdAndBranchIdAndIsDeletedFalse(parameterId, BRANCH_ID)).thenReturn(true);
        when(referenceRangeRepository.findAllByParameterIdAndBranchIdAndIsDeletedFalse(parameterId, BRANCH_ID))
                .thenReturn(List.of(range));
        when(referenceRangeMapper.toResponse(range)).thenReturn(response);

        List<ReferenceRangeResponse> result = referenceRangeService.getByParameterId(parameterId);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst()).isEqualTo(response);
    }

    @Test
    @DisplayName("getByParameterId with parameter not found should throw NotFoundException")
    void getByParameterId_parameterNotFound_shouldThrowNotFoundException() {
        UUID parameterId = UUID.randomUUID();

        when(parameterRepository.existsByIdAndBranchIdAndIsDeletedFalse(parameterId, BRANCH_ID)).thenReturn(false);

        assertThatThrownBy(() -> referenceRangeService.getByParameterId(parameterId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Parameter");
    }

    @Test
    @DisplayName("update should update and return reference range")
    void update_shouldUpdateAndReturnReferenceRange() {
        UUID rangeId = UUID.randomUUID();
        UUID parameterId = UUID.randomUUID();
        ReferenceRangeRequest request = new ReferenceRangeRequest();
        request.setParameterId(parameterId);
        request.setAgeMin(BigDecimal.ZERO);
        request.setAgeMax(BigDecimal.TEN);
        request.setGender("F");
        ReferenceRange range = new ReferenceRange();
        Parameter parameter = new Parameter();
        ReferenceRange saved = new ReferenceRange();
        ReferenceRangeResponse response = new ReferenceRangeResponse();

        when(referenceRangeRepository.findByIdAndBranchIdAndIsDeletedFalse(rangeId, BRANCH_ID)).thenReturn(Optional.of(range));
        when(parameterRepository.findByIdAndBranchIdAndIsDeletedFalse(parameterId, BRANCH_ID)).thenReturn(Optional.of(parameter));
        when(referenceRangeRepository.findAllByParameterIdAndGenderAndBranchIdAndIsDeletedFalse(parameterId, "F", BRANCH_ID))
                .thenReturn(Collections.emptyList());
        when(referenceRangeRepository.save(range)).thenReturn(saved);
        when(referenceRangeMapper.toResponse(saved)).thenReturn(response);

        ReferenceRangeResponse result = referenceRangeService.update(rangeId, request);

        assertThat(result).isEqualTo(response);
        verify(referenceRangeMapper).updateEntity(request, range);
    }

    @Test
    @DisplayName("update not found should throw NotFoundException")
    void update_notFound_shouldThrowNotFoundException() {
        UUID rangeId = UUID.randomUUID();
        ReferenceRangeRequest request = new ReferenceRangeRequest();

        when(referenceRangeRepository.findByIdAndBranchIdAndIsDeletedFalse(rangeId, BRANCH_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> referenceRangeService.update(rangeId, request))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("ReferenceRange");
    }

    @Test
    @DisplayName("delete should soft delete reference range")
    void delete_shouldSoftDelete() {
        UUID rangeId = UUID.randomUUID();
        ReferenceRange range = new ReferenceRange();
        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken("admin", null));

        when(referenceRangeRepository.findByIdAndBranchIdAndIsDeletedFalse(rangeId, BRANCH_ID)).thenReturn(Optional.of(range));

        referenceRangeService.delete(rangeId);

        assertThat(range.getIsDeleted()).isTrue();
        assertThat(range.getDeletedAt()).isNotNull();
        verify(referenceRangeRepository).save(range);
    }

    @Test
    @DisplayName("delete not found should throw NotFoundException")
    void delete_notFound_shouldThrowNotFoundException() {
        UUID rangeId = UUID.randomUUID();

        when(referenceRangeRepository.findByIdAndBranchIdAndIsDeletedFalse(rangeId, BRANCH_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> referenceRangeService.delete(rangeId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("ReferenceRange");
    }
}
