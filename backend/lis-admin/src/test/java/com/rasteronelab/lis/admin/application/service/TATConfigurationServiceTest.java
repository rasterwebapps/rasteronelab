package com.rasteronelab.lis.admin.application.service;

import com.rasteronelab.lis.admin.api.dto.TATConfigurationRequest;
import com.rasteronelab.lis.admin.api.dto.TATConfigurationResponse;
import com.rasteronelab.lis.admin.api.mapper.TATConfigurationMapper;
import com.rasteronelab.lis.admin.domain.model.Department;
import com.rasteronelab.lis.admin.domain.model.TATConfiguration;
import com.rasteronelab.lis.admin.domain.model.TestMaster;
import com.rasteronelab.lis.admin.domain.repository.DepartmentRepository;
import com.rasteronelab.lis.admin.domain.repository.TATConfigurationRepository;
import com.rasteronelab.lis.admin.domain.repository.TestMasterRepository;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("TATConfigurationService")
@ExtendWith(MockitoExtension.class)
class TATConfigurationServiceTest {

    private static final UUID BRANCH_ID = UUID.randomUUID();

    @Mock
    private TATConfigurationRepository tatConfigurationRepository;

    @Mock
    private TestMasterRepository testMasterRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private TATConfigurationMapper tatConfigurationMapper;

    @InjectMocks
    private TATConfigurationService tatConfigurationService;

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
    @DisplayName("create with test and department should save and return config")
    void create_withTestAndDepartment_shouldSaveAndReturnConfig() {
        UUID testId = UUID.randomUUID();
        UUID departmentId = UUID.randomUUID();
        TATConfigurationRequest request = new TATConfigurationRequest();
        request.setTestId(testId);
        request.setDepartmentId(departmentId);
        request.setRoutineHours(24);
        TestMaster testMaster = new TestMaster();
        Department department = new Department();
        TATConfiguration config = new TATConfiguration();
        TATConfiguration saved = new TATConfiguration();
        TATConfigurationResponse response = new TATConfigurationResponse();

        when(tatConfigurationMapper.toEntity(request)).thenReturn(config);
        when(testMasterRepository.findByIdAndBranchIdAndIsDeletedFalse(testId, BRANCH_ID)).thenReturn(Optional.of(testMaster));
        when(departmentRepository.findByIdAndIsDeletedFalse(departmentId)).thenReturn(Optional.of(department));
        when(tatConfigurationRepository.save(config)).thenReturn(saved);
        when(tatConfigurationMapper.toResponse(saved)).thenReturn(response);

        TATConfigurationResponse result = tatConfigurationService.create(request);

        assertThat(result).isEqualTo(response);
        assertThat(config.getBranchId()).isEqualTo(BRANCH_ID);
        verify(tatConfigurationRepository).save(config);
    }

    @Test
    @DisplayName("create without test and department should save successfully")
    void create_withoutTestAndDepartment_shouldSaveSuccessfully() {
        TATConfigurationRequest request = new TATConfigurationRequest();
        request.setTestId(null);
        request.setDepartmentId(null);
        request.setRoutineHours(48);
        TATConfiguration config = new TATConfiguration();
        TATConfiguration saved = new TATConfiguration();
        TATConfigurationResponse response = new TATConfigurationResponse();

        when(tatConfigurationMapper.toEntity(request)).thenReturn(config);
        when(tatConfigurationRepository.save(config)).thenReturn(saved);
        when(tatConfigurationMapper.toResponse(saved)).thenReturn(response);

        TATConfigurationResponse result = tatConfigurationService.create(request);

        assertThat(result).isEqualTo(response);
    }

    @Test
    @DisplayName("create with test not found should throw NotFoundException")
    void create_withTestNotFound_shouldThrowNotFoundException() {
        UUID testId = UUID.randomUUID();
        TATConfigurationRequest request = new TATConfigurationRequest();
        request.setTestId(testId);
        TATConfiguration config = new TATConfiguration();

        when(tatConfigurationMapper.toEntity(request)).thenReturn(config);
        when(testMasterRepository.findByIdAndBranchIdAndIsDeletedFalse(testId, BRANCH_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tatConfigurationService.create(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Test");
    }

    @Test
    @DisplayName("create with department not found should throw NotFoundException")
    void create_withDepartmentNotFound_shouldThrowNotFoundException() {
        UUID departmentId = UUID.randomUUID();
        TATConfigurationRequest request = new TATConfigurationRequest();
        request.setTestId(null);
        request.setDepartmentId(departmentId);
        TATConfiguration config = new TATConfiguration();

        when(tatConfigurationMapper.toEntity(request)).thenReturn(config);
        when(departmentRepository.findByIdAndIsDeletedFalse(departmentId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tatConfigurationService.create(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Department");
    }

    @Test
    @DisplayName("getById should return config")
    void getById_shouldReturnConfig() {
        UUID configId = UUID.randomUUID();
        TATConfiguration config = new TATConfiguration();
        TATConfigurationResponse response = new TATConfigurationResponse();

        when(tatConfigurationRepository.findByIdAndBranchIdAndIsDeletedFalse(configId, BRANCH_ID)).thenReturn(Optional.of(config));
        when(tatConfigurationMapper.toResponse(config)).thenReturn(response);

        TATConfigurationResponse result = tatConfigurationService.getById(configId);

        assertThat(result).isEqualTo(response);
    }

    @Test
    @DisplayName("getById not found should throw NotFoundException")
    void getById_notFound_shouldThrowNotFoundException() {
        UUID configId = UUID.randomUUID();

        when(tatConfigurationRepository.findByIdAndBranchIdAndIsDeletedFalse(configId, BRANCH_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tatConfigurationService.getById(configId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("TATConfiguration");
    }

    @Test
    @DisplayName("getAll should return paged results")
    void getAll_shouldReturnPagedResults() {
        Pageable pageable = PageRequest.of(0, 10);
        TATConfiguration config = new TATConfiguration();
        TATConfigurationResponse response = new TATConfigurationResponse();
        Page<TATConfiguration> configPage = new PageImpl<>(List.of(config));

        when(tatConfigurationRepository.findAllByBranchIdAndIsDeletedFalse(BRANCH_ID, pageable)).thenReturn(configPage);
        when(tatConfigurationMapper.toResponse(config)).thenReturn(response);

        Page<TATConfigurationResponse> result = tatConfigurationService.getAll(pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst()).isEqualTo(response);
    }

    @Test
    @DisplayName("getByTest should return paged results for test")
    void getByTest_shouldReturnPagedResults() {
        UUID testId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10);
        TATConfiguration config = new TATConfiguration();
        TATConfigurationResponse response = new TATConfigurationResponse();
        Page<TATConfiguration> configPage = new PageImpl<>(List.of(config));
        TestMaster testMaster = new TestMaster();

        when(testMasterRepository.findByIdAndBranchIdAndIsDeletedFalse(testId, BRANCH_ID)).thenReturn(Optional.of(testMaster));
        when(tatConfigurationRepository.findAllByBranchIdAndTestIdAndIsDeletedFalse(BRANCH_ID, testId, pageable)).thenReturn(configPage);
        when(tatConfigurationMapper.toResponse(config)).thenReturn(response);

        Page<TATConfigurationResponse> result = tatConfigurationService.getByTest(testId, pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst()).isEqualTo(response);
    }

    @Test
    @DisplayName("getByTest with test not found should throw NotFoundException")
    void getByTest_testNotFound_shouldThrowNotFoundException() {
        UUID testId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10);

        when(testMasterRepository.findByIdAndBranchIdAndIsDeletedFalse(testId, BRANCH_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tatConfigurationService.getByTest(testId, pageable))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Test");
    }

    @Test
    @DisplayName("getByDepartment should return paged results for department")
    void getByDepartment_shouldReturnPagedResults() {
        UUID departmentId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10);
        TATConfiguration config = new TATConfiguration();
        TATConfigurationResponse response = new TATConfigurationResponse();
        Page<TATConfiguration> configPage = new PageImpl<>(List.of(config));
        Department department = new Department();

        when(departmentRepository.findByIdAndIsDeletedFalse(departmentId)).thenReturn(Optional.of(department));
        when(tatConfigurationRepository.findAllByBranchIdAndDepartmentIdAndIsDeletedFalse(BRANCH_ID, departmentId, pageable)).thenReturn(configPage);
        when(tatConfigurationMapper.toResponse(config)).thenReturn(response);

        Page<TATConfigurationResponse> result = tatConfigurationService.getByDepartment(departmentId, pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst()).isEqualTo(response);
    }

    @Test
    @DisplayName("getByDepartment with department not found should throw NotFoundException")
    void getByDepartment_departmentNotFound_shouldThrowNotFoundException() {
        UUID departmentId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10);

        when(departmentRepository.findByIdAndIsDeletedFalse(departmentId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tatConfigurationService.getByDepartment(departmentId, pageable))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Department");
    }

    @Test
    @DisplayName("update with test and department should update and return config")
    void update_withTestAndDepartment_shouldUpdateAndReturnConfig() {
        UUID configId = UUID.randomUUID();
        UUID testId = UUID.randomUUID();
        UUID departmentId = UUID.randomUUID();
        TATConfigurationRequest request = new TATConfigurationRequest();
        request.setTestId(testId);
        request.setDepartmentId(departmentId);
        TATConfiguration config = new TATConfiguration();
        TestMaster testMaster = new TestMaster();
        Department department = new Department();
        TATConfiguration saved = new TATConfiguration();
        TATConfigurationResponse response = new TATConfigurationResponse();

        when(tatConfigurationRepository.findByIdAndBranchIdAndIsDeletedFalse(configId, BRANCH_ID)).thenReturn(Optional.of(config));
        when(testMasterRepository.findByIdAndBranchIdAndIsDeletedFalse(testId, BRANCH_ID)).thenReturn(Optional.of(testMaster));
        when(departmentRepository.findByIdAndIsDeletedFalse(departmentId)).thenReturn(Optional.of(department));
        when(tatConfigurationRepository.save(config)).thenReturn(saved);
        when(tatConfigurationMapper.toResponse(saved)).thenReturn(response);

        TATConfigurationResponse result = tatConfigurationService.update(configId, request);

        assertThat(result).isEqualTo(response);
        verify(tatConfigurationMapper).updateEntity(request, config);
    }

    @Test
    @DisplayName("update with null test and department should clear associations")
    void update_withNullTestAndDepartment_shouldClearAssociations() {
        UUID configId = UUID.randomUUID();
        TATConfigurationRequest request = new TATConfigurationRequest();
        request.setTestId(null);
        request.setDepartmentId(null);
        TATConfiguration config = new TATConfiguration();
        TATConfiguration saved = new TATConfiguration();
        TATConfigurationResponse response = new TATConfigurationResponse();

        when(tatConfigurationRepository.findByIdAndBranchIdAndIsDeletedFalse(configId, BRANCH_ID)).thenReturn(Optional.of(config));
        when(tatConfigurationRepository.save(config)).thenReturn(saved);
        when(tatConfigurationMapper.toResponse(saved)).thenReturn(response);

        TATConfigurationResponse result = tatConfigurationService.update(configId, request);

        assertThat(result).isEqualTo(response);
        assertThat(config.getTest()).isNull();
        assertThat(config.getDepartment()).isNull();
    }

    @Test
    @DisplayName("update not found should throw NotFoundException")
    void update_notFound_shouldThrowNotFoundException() {
        UUID configId = UUID.randomUUID();
        TATConfigurationRequest request = new TATConfigurationRequest();

        when(tatConfigurationRepository.findByIdAndBranchIdAndIsDeletedFalse(configId, BRANCH_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tatConfigurationService.update(configId, request))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("TATConfiguration");
    }

    @Test
    @DisplayName("delete should soft delete config")
    void delete_shouldSoftDelete() {
        UUID configId = UUID.randomUUID();
        TATConfiguration config = new TATConfiguration();
        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken("admin", null));

        when(tatConfigurationRepository.findByIdAndBranchIdAndIsDeletedFalse(configId, BRANCH_ID)).thenReturn(Optional.of(config));

        tatConfigurationService.delete(configId);

        assertThat(config.getIsDeleted()).isTrue();
        assertThat(config.getDeletedAt()).isNotNull();
        verify(tatConfigurationRepository).save(config);
    }

    @Test
    @DisplayName("delete not found should throw NotFoundException")
    void delete_notFound_shouldThrowNotFoundException() {
        UUID configId = UUID.randomUUID();

        when(tatConfigurationRepository.findByIdAndBranchIdAndIsDeletedFalse(configId, BRANCH_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tatConfigurationService.delete(configId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("TATConfiguration");
    }
}
