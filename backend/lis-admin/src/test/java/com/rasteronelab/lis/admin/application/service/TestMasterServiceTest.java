package com.rasteronelab.lis.admin.application.service;

import com.rasteronelab.lis.admin.api.dto.TestMasterRequest;
import com.rasteronelab.lis.admin.api.dto.TestMasterResponse;
import com.rasteronelab.lis.admin.api.mapper.TestMasterMapper;
import com.rasteronelab.lis.admin.domain.model.Department;
import com.rasteronelab.lis.admin.domain.model.TestMaster;
import com.rasteronelab.lis.admin.domain.repository.DepartmentRepository;
import com.rasteronelab.lis.admin.domain.repository.TestMasterRepository;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("TestMasterService")
@ExtendWith(MockitoExtension.class)
class TestMasterServiceTest {

    private static final UUID BRANCH_ID = UUID.randomUUID();
    private static final UUID DEPARTMENT_ID = UUID.randomUUID();

    @Mock
    private TestMasterRepository testMasterRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private TestMasterMapper testMasterMapper;

    @InjectMocks
    private TestMasterService testMasterService;

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
    @DisplayName("create should save and return test master")
    void create_shouldSaveAndReturnTestMaster() {
        TestMasterRequest request = new TestMasterRequest();
        request.setCode("TST-001");
        request.setDepartmentId(DEPARTMENT_ID);
        Department department = new Department();
        TestMaster testMaster = new TestMaster();
        TestMaster saved = new TestMaster();
        TestMasterResponse response = new TestMasterResponse();

        when(departmentRepository.findByIdAndIsDeletedFalse(DEPARTMENT_ID)).thenReturn(Optional.of(department));
        when(testMasterRepository.existsByCodeAndBranchIdAndIsDeletedFalse("TST-001", BRANCH_ID)).thenReturn(false);
        when(testMasterMapper.toEntity(request)).thenReturn(testMaster);
        when(testMasterRepository.save(testMaster)).thenReturn(saved);
        when(testMasterMapper.toResponse(saved)).thenReturn(response);

        TestMasterResponse result = testMasterService.create(request);

        assertThat(result).isEqualTo(response);
        assertThat(testMaster.getBranchId()).isEqualTo(BRANCH_ID);
        assertThat(testMaster.getDepartment()).isEqualTo(department);
        verify(testMasterRepository).save(testMaster);
    }

    @Test
    @DisplayName("create with duplicate code should throw DuplicateResourceException")
    void create_withDuplicateCode_shouldThrowDuplicateResourceException() {
        TestMasterRequest request = new TestMasterRequest();
        request.setCode("TST-001");
        request.setDepartmentId(DEPARTMENT_ID);
        Department department = new Department();

        when(departmentRepository.findByIdAndIsDeletedFalse(DEPARTMENT_ID)).thenReturn(Optional.of(department));
        when(testMasterRepository.existsByCodeAndBranchIdAndIsDeletedFalse("TST-001", BRANCH_ID)).thenReturn(true);

        assertThatThrownBy(() -> testMasterService.create(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("code");
    }

    @Test
    @DisplayName("create with invalid department should throw NotFoundException")
    void create_withInvalidDepartment_shouldThrowNotFoundException() {
        TestMasterRequest request = new TestMasterRequest();
        request.setDepartmentId(DEPARTMENT_ID);

        when(departmentRepository.findByIdAndIsDeletedFalse(DEPARTMENT_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> testMasterService.create(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Department");
    }

    @Test
    @DisplayName("getById should return test master")
    void getById_shouldReturnTestMaster() {
        UUID testId = UUID.randomUUID();
        TestMaster testMaster = new TestMaster();
        TestMasterResponse response = new TestMasterResponse();

        when(testMasterRepository.findByIdAndBranchIdAndIsDeletedFalse(testId, BRANCH_ID)).thenReturn(Optional.of(testMaster));
        when(testMasterMapper.toResponse(testMaster)).thenReturn(response);

        TestMasterResponse result = testMasterService.getById(testId);

        assertThat(result).isEqualTo(response);
    }

    @Test
    @DisplayName("getById not found should throw NotFoundException")
    void getById_notFound_shouldThrowNotFoundException() {
        UUID testId = UUID.randomUUID();

        when(testMasterRepository.findByIdAndBranchIdAndIsDeletedFalse(testId, BRANCH_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> testMasterService.getById(testId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Test");
    }

    @Test
    @DisplayName("getAll should return paged results")
    void getAll_shouldReturnPagedResults() {
        Pageable pageable = PageRequest.of(0, 10);
        TestMaster testMaster = new TestMaster();
        TestMasterResponse response = new TestMasterResponse();
        Page<TestMaster> testPage = new PageImpl<>(List.of(testMaster));

        when(testMasterRepository.findAllByBranchIdAndIsDeletedFalse(BRANCH_ID, pageable)).thenReturn(testPage);
        when(testMasterMapper.toResponse(testMaster)).thenReturn(response);

        Page<TestMasterResponse> result = testMasterService.getAll(pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst()).isEqualTo(response);
    }

    @Test
    @DisplayName("search should return paged results matching query")
    void search_shouldReturnPagedResults() {
        Pageable pageable = PageRequest.of(0, 10);
        String query = "blood";
        TestMaster testMaster = new TestMaster();
        TestMasterResponse response = new TestMasterResponse();
        Page<TestMaster> testPage = new PageImpl<>(List.of(testMaster));

        when(testMasterRepository.findAllByBranchIdAndIsDeletedFalseAndNameContainingIgnoreCase(BRANCH_ID, query, pageable)).thenReturn(testPage);
        when(testMasterMapper.toResponse(testMaster)).thenReturn(response);

        Page<TestMasterResponse> result = testMasterService.search(query, pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst()).isEqualTo(response);
    }

    @Test
    @DisplayName("getByDepartment should return paged results")
    void getByDepartment_shouldReturnPagedResults() {
        Pageable pageable = PageRequest.of(0, 10);
        TestMaster testMaster = new TestMaster();
        TestMasterResponse response = new TestMasterResponse();
        Page<TestMaster> testPage = new PageImpl<>(List.of(testMaster));

        when(departmentRepository.existsById(DEPARTMENT_ID)).thenReturn(true);
        when(testMasterRepository.findAllByBranchIdAndDepartmentIdAndIsDeletedFalse(BRANCH_ID, DEPARTMENT_ID, pageable)).thenReturn(testPage);
        when(testMasterMapper.toResponse(testMaster)).thenReturn(response);

        Page<TestMasterResponse> result = testMasterService.getByDepartment(DEPARTMENT_ID, pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst()).isEqualTo(response);
    }

    @Test
    @DisplayName("getByDepartment with invalid department should throw NotFoundException")
    void getByDepartment_withInvalidDepartment_shouldThrowNotFoundException() {
        Pageable pageable = PageRequest.of(0, 10);

        when(departmentRepository.existsById(DEPARTMENT_ID)).thenReturn(false);

        assertThatThrownBy(() -> testMasterService.getByDepartment(DEPARTMENT_ID, pageable))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Department");
    }

    @Test
    @DisplayName("update should update and return test master")
    void update_shouldUpdateAndReturnTestMaster() {
        UUID testId = UUID.randomUUID();
        TestMasterRequest request = new TestMasterRequest();
        request.setCode("TST-002");
        request.setDepartmentId(DEPARTMENT_ID);
        Department department = new Department();
        TestMaster testMaster = new TestMaster();
        testMaster.setCode("TST-001");
        TestMaster saved = new TestMaster();
        TestMasterResponse response = new TestMasterResponse();

        when(testMasterRepository.findByIdAndBranchIdAndIsDeletedFalse(testId, BRANCH_ID)).thenReturn(Optional.of(testMaster));
        when(departmentRepository.findByIdAndIsDeletedFalse(DEPARTMENT_ID)).thenReturn(Optional.of(department));
        when(testMasterRepository.existsByCodeAndBranchIdAndIsDeletedFalse("TST-002", BRANCH_ID)).thenReturn(false);
        when(testMasterRepository.save(testMaster)).thenReturn(saved);
        when(testMasterMapper.toResponse(saved)).thenReturn(response);

        TestMasterResponse result = testMasterService.update(testId, request);

        assertThat(result).isEqualTo(response);
        verify(testMasterMapper).updateEntity(request, testMaster);
        assertThat(testMaster.getDepartment()).isEqualTo(department);
    }

    @Test
    @DisplayName("delete should soft delete test master")
    void delete_shouldSoftDelete() {
        UUID testId = UUID.randomUUID();
        TestMaster testMaster = new TestMaster();
        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken("admin", null));

        when(testMasterRepository.findByIdAndBranchIdAndIsDeletedFalse(testId, BRANCH_ID)).thenReturn(Optional.of(testMaster));

        testMasterService.delete(testId);

        assertThat(testMaster.getIsDeleted()).isTrue();
        assertThat(testMaster.getDeletedAt()).isNotNull();
        verify(testMasterRepository).save(testMaster);
    }
}
