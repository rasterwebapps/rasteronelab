package com.rasteronelab.lis.admin.application.service;

import com.rasteronelab.lis.admin.api.dto.RoleRequest;
import com.rasteronelab.lis.admin.api.dto.RoleResponse;
import com.rasteronelab.lis.admin.api.mapper.RoleMapper;
import com.rasteronelab.lis.admin.domain.model.Role;
import com.rasteronelab.lis.admin.domain.repository.RoleRepository;
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

@DisplayName("RoleService")
@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    private static final UUID BRANCH_ID = UUID.randomUUID();

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RoleMapper roleMapper;

    @InjectMocks
    private RoleService roleService;

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
    @DisplayName("create should save and return role")
    void create_shouldSaveAndReturnRole() {
        RoleRequest request = new RoleRequest();
        request.setRoleName("Lab Technician");
        Role role = new Role();
        Role saved = new Role();
        RoleResponse response = new RoleResponse();

        when(roleRepository.existsByRoleNameAndBranchIdAndIsDeletedFalse("Lab Technician", BRANCH_ID)).thenReturn(false);
        when(roleMapper.toEntity(request)).thenReturn(role);
        when(roleRepository.save(role)).thenReturn(saved);
        when(roleMapper.toResponse(saved)).thenReturn(response);

        RoleResponse result = roleService.create(request);

        assertThat(result).isEqualTo(response);
        assertThat(role.getBranchId()).isEqualTo(BRANCH_ID);
        verify(roleRepository).save(role);
    }

    @Test
    @DisplayName("create with duplicate roleName should throw DuplicateResourceException")
    void create_withDuplicateRoleName_shouldThrowDuplicateResourceException() {
        RoleRequest request = new RoleRequest();
        request.setRoleName("Lab Technician");

        when(roleRepository.existsByRoleNameAndBranchIdAndIsDeletedFalse("Lab Technician", BRANCH_ID)).thenReturn(true);

        assertThatThrownBy(() -> roleService.create(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("roleName");
    }

    @Test
    @DisplayName("getById should return role")
    void getById_shouldReturnRole() {
        UUID roleId = UUID.randomUUID();
        Role role = new Role();
        RoleResponse response = new RoleResponse();

        when(roleRepository.findByIdAndBranchIdAndIsDeletedFalse(roleId, BRANCH_ID)).thenReturn(Optional.of(role));
        when(roleMapper.toResponse(role)).thenReturn(response);

        RoleResponse result = roleService.getById(roleId);

        assertThat(result).isEqualTo(response);
    }

    @Test
    @DisplayName("getById not found should throw NotFoundException")
    void getById_notFound_shouldThrowNotFoundException() {
        UUID roleId = UUID.randomUUID();

        when(roleRepository.findByIdAndBranchIdAndIsDeletedFalse(roleId, BRANCH_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> roleService.getById(roleId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Role");
    }

    @Test
    @DisplayName("getAll should return paged results")
    void getAll_shouldReturnPagedResults() {
        Pageable pageable = PageRequest.of(0, 10);
        Role role = new Role();
        RoleResponse response = new RoleResponse();
        Page<Role> rolePage = new PageImpl<>(List.of(role));

        when(roleRepository.findAllByBranchIdAndIsDeletedFalse(BRANCH_ID, pageable)).thenReturn(rolePage);
        when(roleMapper.toResponse(role)).thenReturn(response);

        Page<RoleResponse> result = roleService.getAll(pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst()).isEqualTo(response);
    }

    @Test
    @DisplayName("update should update and return role")
    void update_shouldUpdateAndReturnRole() {
        UUID roleId = UUID.randomUUID();
        RoleRequest request = new RoleRequest();
        request.setRoleName("Senior Technician");
        Role role = new Role();
        role.setRoleName("Lab Technician");
        Role saved = new Role();
        RoleResponse response = new RoleResponse();

        when(roleRepository.findByIdAndBranchIdAndIsDeletedFalse(roleId, BRANCH_ID)).thenReturn(Optional.of(role));
        when(roleRepository.existsByRoleNameAndBranchIdAndIsDeletedFalse("Senior Technician", BRANCH_ID)).thenReturn(false);
        when(roleRepository.save(role)).thenReturn(saved);
        when(roleMapper.toResponse(saved)).thenReturn(response);

        RoleResponse result = roleService.update(roleId, request);

        assertThat(result).isEqualTo(response);
        verify(roleMapper).updateEntity(request, role);
    }

    @Test
    @DisplayName("delete should soft delete role")
    void delete_shouldSoftDelete() {
        UUID roleId = UUID.randomUUID();
        Role role = new Role();
        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken("admin", null));

        when(roleRepository.findByIdAndBranchIdAndIsDeletedFalse(roleId, BRANCH_ID)).thenReturn(Optional.of(role));

        roleService.delete(roleId);

        assertThat(role.getIsDeleted()).isTrue();
        assertThat(role.getDeletedAt()).isNotNull();
        verify(roleRepository).save(role);
    }
}
