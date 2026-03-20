package com.rasteronelab.lis.admin.application.service;

import com.rasteronelab.lis.admin.api.dto.ParameterRequest;
import com.rasteronelab.lis.admin.api.dto.ParameterResponse;
import com.rasteronelab.lis.admin.api.mapper.ParameterMapper;
import com.rasteronelab.lis.admin.domain.model.Parameter;
import com.rasteronelab.lis.admin.domain.repository.ParameterRepository;
import com.rasteronelab.lis.core.common.exception.DuplicateResourceException;
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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("ParameterService")
@ExtendWith(MockitoExtension.class)
class ParameterServiceTest {

    private static final UUID BRANCH_ID = UUID.randomUUID();

    @Mock
    private ParameterRepository parameterRepository;

    @Mock
    private ParameterMapper parameterMapper;

    @InjectMocks
    private ParameterService parameterService;

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
    @DisplayName("create should save and return parameter")
    void create_shouldSaveAndReturnParameter() {
        ParameterRequest request = new ParameterRequest();
        request.setCode("PARAM-001");
        Parameter parameter = new Parameter();
        Parameter saved = new Parameter();
        ParameterResponse response = new ParameterResponse();

        when(parameterRepository.existsByCodeAndBranchIdAndIsDeletedFalse("PARAM-001", BRANCH_ID)).thenReturn(false);
        when(parameterMapper.toEntity(request)).thenReturn(parameter);
        when(parameterRepository.save(parameter)).thenReturn(saved);
        when(parameterMapper.toResponse(saved)).thenReturn(response);

        ParameterResponse result = parameterService.create(request);

        assertThat(result).isEqualTo(response);
        assertThat(parameter.getBranchId()).isEqualTo(BRANCH_ID);
        verify(parameterRepository).save(parameter);
    }

    @Test
    @DisplayName("create with duplicate code should throw DuplicateResourceException")
    void create_withDuplicateCode_shouldThrowDuplicateResourceException() {
        ParameterRequest request = new ParameterRequest();
        request.setCode("PARAM-001");

        when(parameterRepository.existsByCodeAndBranchIdAndIsDeletedFalse("PARAM-001", BRANCH_ID)).thenReturn(true);

        assertThatThrownBy(() -> parameterService.create(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("code");
    }

    @Test
    @DisplayName("getById should return parameter")
    void getById_shouldReturnParameter() {
        UUID parameterId = UUID.randomUUID();
        Parameter parameter = new Parameter();
        ParameterResponse response = new ParameterResponse();

        when(parameterRepository.findByIdAndBranchIdAndIsDeletedFalse(parameterId, BRANCH_ID)).thenReturn(Optional.of(parameter));
        when(parameterMapper.toResponse(parameter)).thenReturn(response);

        ParameterResponse result = parameterService.getById(parameterId);

        assertThat(result).isEqualTo(response);
    }

    @Test
    @DisplayName("getById not found should throw NotFoundException")
    void getById_notFound_shouldThrowNotFoundException() {
        UUID parameterId = UUID.randomUUID();

        when(parameterRepository.findByIdAndBranchIdAndIsDeletedFalse(parameterId, BRANCH_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> parameterService.getById(parameterId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Parameter");
    }

    @Test
    @DisplayName("getAll should return paged results")
    void getAll_shouldReturnPagedResults() {
        Pageable pageable = PageRequest.of(0, 10);
        Parameter parameter = new Parameter();
        ParameterResponse response = new ParameterResponse();
        Page<Parameter> parameterPage = new PageImpl<>(List.of(parameter));

        when(parameterRepository.findAllByBranchIdAndIsDeletedFalse(BRANCH_ID, pageable)).thenReturn(parameterPage);
        when(parameterMapper.toResponse(parameter)).thenReturn(response);

        Page<ParameterResponse> result = parameterService.getAll(pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst()).isEqualTo(response);
    }

    @Test
    @DisplayName("getActive should return active paged results")
    void getActive_shouldReturnActivePagedResults() {
        Pageable pageable = PageRequest.of(0, 10);
        Parameter parameter = new Parameter();
        ParameterResponse response = new ParameterResponse();
        Page<Parameter> parameterPage = new PageImpl<>(List.of(parameter));

        when(parameterRepository.findAllByBranchIdAndIsActiveAndIsDeletedFalse(BRANCH_ID, true, pageable)).thenReturn(parameterPage);
        when(parameterMapper.toResponse(parameter)).thenReturn(response);

        Page<ParameterResponse> result = parameterService.getActive(pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst()).isEqualTo(response);
    }

    @Test
    @DisplayName("update should update and return parameter")
    void update_shouldUpdateAndReturnParameter() {
        UUID parameterId = UUID.randomUUID();
        ParameterRequest request = new ParameterRequest();
        request.setCode("PARAM-002");
        Parameter parameter = new Parameter();
        parameter.setCode("PARAM-001");
        Parameter saved = new Parameter();
        ParameterResponse response = new ParameterResponse();

        when(parameterRepository.findByIdAndBranchIdAndIsDeletedFalse(parameterId, BRANCH_ID)).thenReturn(Optional.of(parameter));
        when(parameterRepository.existsByCodeAndBranchIdAndIsDeletedFalse("PARAM-002", BRANCH_ID)).thenReturn(false);
        when(parameterRepository.save(parameter)).thenReturn(saved);
        when(parameterMapper.toResponse(saved)).thenReturn(response);

        ParameterResponse result = parameterService.update(parameterId, request);

        assertThat(result).isEqualTo(response);
        verify(parameterMapper).updateEntity(request, parameter);
    }

    @Test
    @DisplayName("update with same code should not check duplicate")
    void update_withSameCode_shouldNotCheckDuplicate() {
        UUID parameterId = UUID.randomUUID();
        ParameterRequest request = new ParameterRequest();
        request.setCode("PARAM-001");
        Parameter parameter = new Parameter();
        parameter.setCode("PARAM-001");
        Parameter saved = new Parameter();
        ParameterResponse response = new ParameterResponse();

        when(parameterRepository.findByIdAndBranchIdAndIsDeletedFalse(parameterId, BRANCH_ID)).thenReturn(Optional.of(parameter));
        when(parameterRepository.save(parameter)).thenReturn(saved);
        when(parameterMapper.toResponse(saved)).thenReturn(response);

        ParameterResponse result = parameterService.update(parameterId, request);

        assertThat(result).isEqualTo(response);
        verify(parameterMapper).updateEntity(request, parameter);
    }

    @Test
    @DisplayName("update with duplicate new code should throw DuplicateResourceException")
    void update_withDuplicateNewCode_shouldThrowDuplicateResourceException() {
        UUID parameterId = UUID.randomUUID();
        ParameterRequest request = new ParameterRequest();
        request.setCode("PARAM-002");
        Parameter parameter = new Parameter();
        parameter.setCode("PARAM-001");

        when(parameterRepository.findByIdAndBranchIdAndIsDeletedFalse(parameterId, BRANCH_ID)).thenReturn(Optional.of(parameter));
        when(parameterRepository.existsByCodeAndBranchIdAndIsDeletedFalse("PARAM-002", BRANCH_ID)).thenReturn(true);

        assertThatThrownBy(() -> parameterService.update(parameterId, request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("code");
    }

    @Test
    @DisplayName("update not found should throw NotFoundException")
    void update_notFound_shouldThrowNotFoundException() {
        UUID parameterId = UUID.randomUUID();
        ParameterRequest request = new ParameterRequest();

        when(parameterRepository.findByIdAndBranchIdAndIsDeletedFalse(parameterId, BRANCH_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> parameterService.update(parameterId, request))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Parameter");
    }

    @Test
    @DisplayName("delete should soft delete parameter")
    void delete_shouldSoftDelete() {
        UUID parameterId = UUID.randomUUID();
        Parameter parameter = new Parameter();
        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken("admin", null));

        when(parameterRepository.findByIdAndBranchIdAndIsDeletedFalse(parameterId, BRANCH_ID)).thenReturn(Optional.of(parameter));

        parameterService.delete(parameterId);

        assertThat(parameter.getIsDeleted()).isTrue();
        assertThat(parameter.getDeletedAt()).isNotNull();
        verify(parameterRepository).save(parameter);
    }

    @Test
    @DisplayName("delete not found should throw NotFoundException")
    void delete_notFound_shouldThrowNotFoundException() {
        UUID parameterId = UUID.randomUUID();

        when(parameterRepository.findByIdAndBranchIdAndIsDeletedFalse(parameterId, BRANCH_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> parameterService.delete(parameterId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Parameter");
    }
}
