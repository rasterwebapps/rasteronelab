package com.rasteronelab.lis.admin.application.service;

import com.rasteronelab.lis.admin.api.dto.DepartmentRequest;
import com.rasteronelab.lis.admin.api.dto.DepartmentResponse;
import com.rasteronelab.lis.admin.api.mapper.DepartmentMapper;
import com.rasteronelab.lis.admin.domain.model.Department;
import com.rasteronelab.lis.admin.domain.model.Organization;
import com.rasteronelab.lis.admin.domain.repository.DepartmentRepository;
import com.rasteronelab.lis.admin.domain.repository.OrganizationRepository;
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

@DisplayName("DepartmentService")
@ExtendWith(MockitoExtension.class)
class DepartmentServiceTest {

    private static final UUID ORG_ID = UUID.randomUUID();

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private OrganizationRepository organizationRepository;

    @Mock
    private DepartmentMapper departmentMapper;

    @InjectMocks
    private DepartmentService departmentService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("create should save and return department")
    void create_shouldSaveAndReturnDepartment() {
        DepartmentRequest request = new DepartmentRequest();
        request.setCode("DEPT-001");
        request.setOrgId(ORG_ID);
        Organization organization = new Organization();
        Department department = new Department();
        Department saved = new Department();
        DepartmentResponse response = new DepartmentResponse();

        when(organizationRepository.findByIdAndIsDeletedFalse(ORG_ID)).thenReturn(Optional.of(organization));
        when(departmentRepository.existsByCodeAndOrgIdAndIsDeletedFalse("DEPT-001", ORG_ID)).thenReturn(false);
        when(departmentMapper.toEntity(request)).thenReturn(department);
        when(departmentRepository.save(department)).thenReturn(saved);
        when(departmentMapper.toResponse(saved)).thenReturn(response);

        DepartmentResponse result = departmentService.create(request);

        assertThat(result).isEqualTo(response);
        assertThat(department.getOrganization()).isEqualTo(organization);
        verify(departmentRepository).save(department);
    }

    @Test
    @DisplayName("create with duplicate code should throw DuplicateResourceException")
    void create_withDuplicateCode_shouldThrowDuplicateResourceException() {
        DepartmentRequest request = new DepartmentRequest();
        request.setCode("DEPT-001");
        request.setOrgId(ORG_ID);
        Organization organization = new Organization();

        when(organizationRepository.findByIdAndIsDeletedFalse(ORG_ID)).thenReturn(Optional.of(organization));
        when(departmentRepository.existsByCodeAndOrgIdAndIsDeletedFalse("DEPT-001", ORG_ID)).thenReturn(true);

        assertThatThrownBy(() -> departmentService.create(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("code");
    }

    @Test
    @DisplayName("create with invalid org should throw NotFoundException")
    void create_withInvalidOrg_shouldThrowNotFoundException() {
        DepartmentRequest request = new DepartmentRequest();
        request.setOrgId(ORG_ID);

        when(organizationRepository.findByIdAndIsDeletedFalse(ORG_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> departmentService.create(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Organization");
    }

    @Test
    @DisplayName("getById should return department")
    void getById_shouldReturnDepartment() {
        UUID deptId = UUID.randomUUID();
        Department department = new Department();
        DepartmentResponse response = new DepartmentResponse();

        when(departmentRepository.findByIdAndIsDeletedFalse(deptId)).thenReturn(Optional.of(department));
        when(departmentMapper.toResponse(department)).thenReturn(response);

        DepartmentResponse result = departmentService.getById(deptId);

        assertThat(result).isEqualTo(response);
    }

    @Test
    @DisplayName("getById not found should throw NotFoundException")
    void getById_notFound_shouldThrowNotFoundException() {
        UUID deptId = UUID.randomUUID();

        when(departmentRepository.findByIdAndIsDeletedFalse(deptId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> departmentService.getById(deptId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Department");
    }

    @Test
    @DisplayName("getByOrganization should return paged results")
    void getByOrganization_shouldReturnPagedResults() {
        Pageable pageable = PageRequest.of(0, 10);
        Department department = new Department();
        DepartmentResponse response = new DepartmentResponse();
        Page<Department> deptPage = new PageImpl<>(List.of(department));

        when(organizationRepository.existsById(ORG_ID)).thenReturn(true);
        when(departmentRepository.findAllByOrgIdAndIsDeletedFalse(ORG_ID, pageable)).thenReturn(deptPage);
        when(departmentMapper.toResponse(department)).thenReturn(response);

        Page<DepartmentResponse> result = departmentService.getByOrganization(ORG_ID, pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst()).isEqualTo(response);
    }

    @Test
    @DisplayName("getByOrganization with invalid org should throw NotFoundException")
    void getByOrganization_withInvalidOrg_shouldThrowNotFoundException() {
        Pageable pageable = PageRequest.of(0, 10);

        when(organizationRepository.existsById(ORG_ID)).thenReturn(false);

        assertThatThrownBy(() -> departmentService.getByOrganization(ORG_ID, pageable))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Organization");
    }

    @Test
    @DisplayName("update should update and return department")
    void update_shouldUpdateAndReturnDepartment() {
        UUID deptId = UUID.randomUUID();
        DepartmentRequest request = new DepartmentRequest();
        request.setCode("DEPT-002");
        request.setOrgId(ORG_ID);
        Organization organization = new Organization();
        Department department = new Department();
        department.setCode("DEPT-001");
        department.setOrgId(ORG_ID);
        Department saved = new Department();
        DepartmentResponse response = new DepartmentResponse();

        when(departmentRepository.findByIdAndIsDeletedFalse(deptId)).thenReturn(Optional.of(department));
        when(organizationRepository.findByIdAndIsDeletedFalse(ORG_ID)).thenReturn(Optional.of(organization));
        when(departmentRepository.existsByCodeAndOrgIdAndIsDeletedFalse("DEPT-002", ORG_ID)).thenReturn(false);
        when(departmentRepository.save(department)).thenReturn(saved);
        when(departmentMapper.toResponse(saved)).thenReturn(response);

        DepartmentResponse result = departmentService.update(deptId, request);

        assertThat(result).isEqualTo(response);
        verify(departmentMapper).updateEntity(request, department);
    }

    @Test
    @DisplayName("delete should soft delete department")
    void delete_shouldSoftDelete() {
        UUID deptId = UUID.randomUUID();
        Department department = new Department();
        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken("admin", null));

        when(departmentRepository.findByIdAndIsDeletedFalse(deptId)).thenReturn(Optional.of(department));

        departmentService.delete(deptId);

        assertThat(department.getIsDeleted()).isTrue();
        assertThat(department.getDeletedAt()).isNotNull();
        verify(departmentRepository).save(department);
    }
}
