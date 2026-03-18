package com.rasteronelab.lis.admin.application.service;

import com.rasteronelab.lis.admin.api.dto.CriticalValueConfigRequest;
import com.rasteronelab.lis.admin.api.dto.CriticalValueConfigResponse;
import com.rasteronelab.lis.admin.api.mapper.CriticalValueConfigMapper;
import com.rasteronelab.lis.admin.domain.model.CriticalValueConfig;
import com.rasteronelab.lis.admin.domain.model.Parameter;
import com.rasteronelab.lis.admin.domain.repository.CriticalValueConfigRepository;
import com.rasteronelab.lis.admin.domain.repository.ParameterRepository;
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
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("CriticalValueConfigService")
@ExtendWith(MockitoExtension.class)
class CriticalValueConfigServiceTest {

    private static final UUID BRANCH_ID = UUID.randomUUID();

    @Mock
    private CriticalValueConfigRepository criticalValueConfigRepository;

    @Mock
    private ParameterRepository parameterRepository;

    @Mock
    private CriticalValueConfigMapper criticalValueConfigMapper;

    @InjectMocks
    private CriticalValueConfigService criticalValueConfigService;

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
    @DisplayName("create should save and return config")
    void create_shouldSaveAndReturnConfig() {
        UUID parameterId = UUID.randomUUID();
        CriticalValueConfigRequest request = new CriticalValueConfigRequest();
        request.setParameterId(parameterId);
        request.setLowThreshold(new BigDecimal("1.0"));
        request.setHighThreshold(new BigDecimal("10.0"));
        Parameter parameter = new Parameter();
        CriticalValueConfig entity = new CriticalValueConfig();
        CriticalValueConfig saved = new CriticalValueConfig();
        CriticalValueConfigResponse response = new CriticalValueConfigResponse();

        when(parameterRepository.findByIdAndBranchIdAndIsDeletedFalse(parameterId, BRANCH_ID)).thenReturn(Optional.of(parameter));
        when(criticalValueConfigMapper.toEntity(request)).thenReturn(entity);
        when(criticalValueConfigRepository.save(entity)).thenReturn(saved);
        when(criticalValueConfigMapper.toResponse(saved)).thenReturn(response);

        CriticalValueConfigResponse result = criticalValueConfigService.create(request);

        assertThat(result).isEqualTo(response);
        assertThat(entity.getBranchId()).isEqualTo(BRANCH_ID);
        verify(criticalValueConfigRepository).save(entity);
    }

    @Test
    @DisplayName("create with invalid parameter should throw NotFoundException")
    void create_withInvalidParameter_shouldThrowNotFoundException() {
        UUID parameterId = UUID.randomUUID();
        CriticalValueConfigRequest request = new CriticalValueConfigRequest();
        request.setParameterId(parameterId);

        when(parameterRepository.findByIdAndBranchIdAndIsDeletedFalse(parameterId, BRANCH_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> criticalValueConfigService.create(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Parameter");
    }

    @Test
    @DisplayName("create with low >= high threshold should throw BusinessRuleException")
    void create_withInvalidThresholds_shouldThrowBusinessRuleException() {
        UUID parameterId = UUID.randomUUID();
        CriticalValueConfigRequest request = new CriticalValueConfigRequest();
        request.setParameterId(parameterId);
        request.setLowThreshold(new BigDecimal("10.0"));
        request.setHighThreshold(new BigDecimal("5.0"));
        Parameter parameter = new Parameter();

        when(parameterRepository.findByIdAndBranchIdAndIsDeletedFalse(parameterId, BRANCH_ID)).thenReturn(Optional.of(parameter));

        assertThatThrownBy(() -> criticalValueConfigService.create(request))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("threshold");
    }

    @Test
    @DisplayName("getById should return config")
    void getById_shouldReturnConfig() {
        UUID id = UUID.randomUUID();
        CriticalValueConfig entity = new CriticalValueConfig();
        CriticalValueConfigResponse response = new CriticalValueConfigResponse();

        when(criticalValueConfigRepository.findByIdAndBranchIdAndIsDeletedFalse(id, BRANCH_ID)).thenReturn(Optional.of(entity));
        when(criticalValueConfigMapper.toResponse(entity)).thenReturn(response);

        CriticalValueConfigResponse result = criticalValueConfigService.getById(id);

        assertThat(result).isEqualTo(response);
    }

    @Test
    @DisplayName("getById not found should throw NotFoundException")
    void getById_notFound_shouldThrowNotFoundException() {
        UUID id = UUID.randomUUID();

        when(criticalValueConfigRepository.findByIdAndBranchIdAndIsDeletedFalse(id, BRANCH_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> criticalValueConfigService.getById(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("CriticalValueConfig");
    }

    @Test
    @DisplayName("delete should soft delete config")
    void delete_shouldSoftDelete() {
        UUID id = UUID.randomUUID();
        CriticalValueConfig entity = new CriticalValueConfig();
        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken("admin", null));

        when(criticalValueConfigRepository.findByIdAndBranchIdAndIsDeletedFalse(id, BRANCH_ID)).thenReturn(Optional.of(entity));

        criticalValueConfigService.delete(id);

        assertThat(entity.getIsDeleted()).isTrue();
        assertThat(entity.getDeletedAt()).isNotNull();
        verify(criticalValueConfigRepository).save(entity);
    }
}
