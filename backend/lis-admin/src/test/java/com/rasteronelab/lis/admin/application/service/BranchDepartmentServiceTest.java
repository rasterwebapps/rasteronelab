package com.rasteronelab.lis.admin.application.service;

import com.rasteronelab.lis.admin.api.dto.BranchDepartmentRequest;
import com.rasteronelab.lis.admin.api.dto.BranchDepartmentResponse;
import com.rasteronelab.lis.admin.api.mapper.BranchDepartmentMapper;
import com.rasteronelab.lis.admin.domain.model.Branch;
import com.rasteronelab.lis.admin.domain.model.BranchDepartment;
import com.rasteronelab.lis.admin.domain.model.Department;
import com.rasteronelab.lis.admin.domain.repository.BranchDepartmentRepository;
import com.rasteronelab.lis.admin.domain.repository.BranchRepository;
import com.rasteronelab.lis.admin.domain.repository.DepartmentRepository;
import com.rasteronelab.lis.core.common.exception.DuplicateResourceException;
import com.rasteronelab.lis.core.common.exception.NotFoundException;
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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("BranchDepartmentService")
@ExtendWith(MockitoExtension.class)
class BranchDepartmentServiceTest {

    private static final UUID BRANCH_ID = UUID.randomUUID();
    private static final UUID DEPARTMENT_ID = UUID.randomUUID();

    @Mock
    private BranchDepartmentRepository branchDepartmentRepository;

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private BranchDepartmentMapper branchDepartmentMapper;

    @InjectMocks
    private BranchDepartmentService branchDepartmentService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("assign should save and return branch department mapping")
    void assign_shouldSaveAndReturnMapping() {
        BranchDepartmentRequest request = new BranchDepartmentRequest();
        request.setBranchId(BRANCH_ID);
        request.setDepartmentId(DEPARTMENT_ID);
        request.setIsActive(true);
        Branch branch = new Branch();
        Department department = new Department();
        BranchDepartmentResponse response = new BranchDepartmentResponse();

        when(branchRepository.findByIdAndIsDeletedFalse(BRANCH_ID)).thenReturn(Optional.of(branch));
        when(departmentRepository.findByIdAndIsDeletedFalse(DEPARTMENT_ID)).thenReturn(Optional.of(department));
        when(branchDepartmentRepository.existsByBranchIdAndDepartmentIdAndIsDeletedFalse(BRANCH_ID, DEPARTMENT_ID)).thenReturn(false);
        when(branchDepartmentRepository.save(any(BranchDepartment.class))).thenAnswer(inv -> inv.getArgument(0));
        when(branchDepartmentMapper.toResponse(any(BranchDepartment.class))).thenReturn(response);

        BranchDepartmentResponse result = branchDepartmentService.assign(request);

        assertThat(result).isEqualTo(response);
        verify(branchDepartmentRepository).save(any(BranchDepartment.class));
    }

    @Test
    @DisplayName("assign with duplicate mapping should throw DuplicateResourceException")
    void assign_withDuplicate_shouldThrowDuplicateResourceException() {
        BranchDepartmentRequest request = new BranchDepartmentRequest();
        request.setBranchId(BRANCH_ID);
        request.setDepartmentId(DEPARTMENT_ID);
        Branch branch = new Branch();
        Department department = new Department();

        when(branchRepository.findByIdAndIsDeletedFalse(BRANCH_ID)).thenReturn(Optional.of(branch));
        when(departmentRepository.findByIdAndIsDeletedFalse(DEPARTMENT_ID)).thenReturn(Optional.of(department));
        when(branchDepartmentRepository.existsByBranchIdAndDepartmentIdAndIsDeletedFalse(BRANCH_ID, DEPARTMENT_ID)).thenReturn(true);

        assertThatThrownBy(() -> branchDepartmentService.assign(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("departmentId");
    }

    @Test
    @DisplayName("assign with invalid branch should throw NotFoundException")
    void assign_withInvalidBranch_shouldThrowNotFoundException() {
        BranchDepartmentRequest request = new BranchDepartmentRequest();
        request.setBranchId(BRANCH_ID);
        request.setDepartmentId(DEPARTMENT_ID);

        when(branchRepository.findByIdAndIsDeletedFalse(BRANCH_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> branchDepartmentService.assign(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Branch");
    }

    @Test
    @DisplayName("assign with invalid department should throw NotFoundException")
    void assign_withInvalidDepartment_shouldThrowNotFoundException() {
        BranchDepartmentRequest request = new BranchDepartmentRequest();
        request.setBranchId(BRANCH_ID);
        request.setDepartmentId(DEPARTMENT_ID);
        Branch branch = new Branch();

        when(branchRepository.findByIdAndIsDeletedFalse(BRANCH_ID)).thenReturn(Optional.of(branch));
        when(departmentRepository.findByIdAndIsDeletedFalse(DEPARTMENT_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> branchDepartmentService.assign(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Department");
    }

    @Test
    @DisplayName("getById should return branch department mapping")
    void getById_shouldReturnMapping() {
        UUID mappingId = UUID.randomUUID();
        BranchDepartment mapping = new BranchDepartment();
        BranchDepartmentResponse response = new BranchDepartmentResponse();

        when(branchDepartmentRepository.findById(mappingId)).thenReturn(Optional.of(mapping));
        when(branchDepartmentMapper.toResponse(mapping)).thenReturn(response);

        BranchDepartmentResponse result = branchDepartmentService.getById(mappingId);

        assertThat(result).isEqualTo(response);
    }

    @Test
    @DisplayName("getById not found should throw NotFoundException")
    void getById_notFound_shouldThrowNotFoundException() {
        UUID mappingId = UUID.randomUUID();

        when(branchDepartmentRepository.findById(mappingId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> branchDepartmentService.getById(mappingId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("BranchDepartment");
    }

    @Test
    @DisplayName("getById with deleted mapping should throw NotFoundException")
    void getById_withDeletedMapping_shouldThrowNotFoundException() {
        UUID mappingId = UUID.randomUUID();
        BranchDepartment mapping = new BranchDepartment();
        mapping.setIsDeleted(true);

        when(branchDepartmentRepository.findById(mappingId)).thenReturn(Optional.of(mapping));

        assertThatThrownBy(() -> branchDepartmentService.getById(mappingId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("BranchDepartment");
    }

    @Test
    @DisplayName("getByBranch should return list of mappings")
    void getByBranch_shouldReturnList() {
        BranchDepartment mapping = new BranchDepartment();
        BranchDepartmentResponse response = new BranchDepartmentResponse();

        when(branchRepository.existsById(BRANCH_ID)).thenReturn(true);
        when(branchDepartmentRepository.findAllByBranchIdAndIsDeletedFalse(BRANCH_ID)).thenReturn(List.of(mapping));
        when(branchDepartmentMapper.toResponse(mapping)).thenReturn(response);

        List<BranchDepartmentResponse> result = branchDepartmentService.getByBranch(BRANCH_ID);

        assertThat(result).hasSize(1);
        assertThat(result.getFirst()).isEqualTo(response);
    }

    @Test
    @DisplayName("getByBranch with invalid branch should throw NotFoundException")
    void getByBranch_withInvalidBranch_shouldThrowNotFoundException() {
        when(branchRepository.existsById(BRANCH_ID)).thenReturn(false);

        assertThatThrownBy(() -> branchDepartmentService.getByBranch(BRANCH_ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Branch");
    }

    @Test
    @DisplayName("unassign should soft delete mapping")
    void unassign_shouldSoftDelete() {
        UUID mappingId = UUID.randomUUID();
        BranchDepartment mapping = new BranchDepartment();
        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken("admin", null));

        when(branchDepartmentRepository.findById(mappingId)).thenReturn(Optional.of(mapping));

        branchDepartmentService.unassign(mappingId);

        assertThat(mapping.getIsDeleted()).isTrue();
        assertThat(mapping.getDeletedAt()).isNotNull();
        verify(branchDepartmentRepository).save(mapping);
    }
}
