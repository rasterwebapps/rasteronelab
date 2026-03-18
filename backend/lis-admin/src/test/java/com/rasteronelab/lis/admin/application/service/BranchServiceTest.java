package com.rasteronelab.lis.admin.application.service;

import com.rasteronelab.lis.admin.api.dto.BranchRequest;
import com.rasteronelab.lis.admin.api.dto.BranchResponse;
import com.rasteronelab.lis.admin.api.mapper.BranchMapper;
import com.rasteronelab.lis.admin.domain.model.Branch;
import com.rasteronelab.lis.admin.domain.model.Organization;
import com.rasteronelab.lis.admin.domain.repository.BranchRepository;
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

@DisplayName("BranchService")
@ExtendWith(MockitoExtension.class)
class BranchServiceTest {

    private static final UUID ORG_ID = UUID.randomUUID();

    @Mock
    private BranchRepository branchRepository;

    @Mock
    private OrganizationRepository organizationRepository;

    @Mock
    private BranchMapper branchMapper;

    @InjectMocks
    private BranchService branchService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("create should save and return branch")
    void create_shouldSaveAndReturnBranch() {
        BranchRequest request = new BranchRequest();
        request.setCode("BR-001");
        request.setOrgId(ORG_ID);
        Organization organization = new Organization();
        Branch branch = new Branch();
        Branch saved = new Branch();
        BranchResponse response = new BranchResponse();

        when(organizationRepository.findByIdAndIsDeletedFalse(ORG_ID)).thenReturn(Optional.of(organization));
        when(branchRepository.existsByCodeAndOrgIdAndIsDeletedFalse("BR-001", ORG_ID)).thenReturn(false);
        when(branchMapper.toEntity(request)).thenReturn(branch);
        when(branchRepository.save(branch)).thenReturn(saved);
        when(branchMapper.toResponse(saved)).thenReturn(response);

        BranchResponse result = branchService.create(request);

        assertThat(result).isEqualTo(response);
        assertThat(branch.getOrganization()).isEqualTo(organization);
        verify(branchRepository).save(branch);
    }

    @Test
    @DisplayName("create with duplicate code should throw DuplicateResourceException")
    void create_withDuplicateCode_shouldThrowDuplicateResourceException() {
        BranchRequest request = new BranchRequest();
        request.setCode("BR-001");
        request.setOrgId(ORG_ID);
        Organization organization = new Organization();

        when(organizationRepository.findByIdAndIsDeletedFalse(ORG_ID)).thenReturn(Optional.of(organization));
        when(branchRepository.existsByCodeAndOrgIdAndIsDeletedFalse("BR-001", ORG_ID)).thenReturn(true);

        assertThatThrownBy(() -> branchService.create(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("code");
    }

    @Test
    @DisplayName("create with invalid org should throw NotFoundException")
    void create_withInvalidOrg_shouldThrowNotFoundException() {
        BranchRequest request = new BranchRequest();
        request.setOrgId(ORG_ID);

        when(organizationRepository.findByIdAndIsDeletedFalse(ORG_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> branchService.create(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Organization");
    }

    @Test
    @DisplayName("getById should return branch")
    void getById_shouldReturnBranch() {
        UUID branchId = UUID.randomUUID();
        Branch branch = new Branch();
        BranchResponse response = new BranchResponse();

        when(branchRepository.findByIdAndIsDeletedFalse(branchId)).thenReturn(Optional.of(branch));
        when(branchMapper.toResponse(branch)).thenReturn(response);

        BranchResponse result = branchService.getById(branchId);

        assertThat(result).isEqualTo(response);
    }

    @Test
    @DisplayName("getById not found should throw NotFoundException")
    void getById_notFound_shouldThrowNotFoundException() {
        UUID branchId = UUID.randomUUID();

        when(branchRepository.findByIdAndIsDeletedFalse(branchId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> branchService.getById(branchId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Branch");
    }

    @Test
    @DisplayName("getAll should return paged results")
    void getAll_shouldReturnPagedResults() {
        Pageable pageable = PageRequest.of(0, 10);
        Branch branch = new Branch();
        BranchResponse response = new BranchResponse();
        Page<Branch> branchPage = new PageImpl<>(List.of(branch));

        when(branchRepository.findAllByIsDeletedFalse(pageable)).thenReturn(branchPage);
        when(branchMapper.toResponse(branch)).thenReturn(response);

        Page<BranchResponse> result = branchService.getAll(pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst()).isEqualTo(response);
    }

    @Test
    @DisplayName("getByOrganization should return paged results")
    void getByOrganization_shouldReturnPagedResults() {
        Pageable pageable = PageRequest.of(0, 10);
        Branch branch = new Branch();
        BranchResponse response = new BranchResponse();
        Page<Branch> branchPage = new PageImpl<>(List.of(branch));

        when(organizationRepository.existsById(ORG_ID)).thenReturn(true);
        when(branchRepository.findAllByOrgIdAndIsDeletedFalse(ORG_ID, pageable)).thenReturn(branchPage);
        when(branchMapper.toResponse(branch)).thenReturn(response);

        Page<BranchResponse> result = branchService.getByOrganization(ORG_ID, pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst()).isEqualTo(response);
    }

    @Test
    @DisplayName("getByOrganization with invalid org should throw NotFoundException")
    void getByOrganization_withInvalidOrg_shouldThrowNotFoundException() {
        Pageable pageable = PageRequest.of(0, 10);

        when(organizationRepository.existsById(ORG_ID)).thenReturn(false);

        assertThatThrownBy(() -> branchService.getByOrganization(ORG_ID, pageable))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Organization");
    }

    @Test
    @DisplayName("update should update and return branch")
    void update_shouldUpdateAndReturnBranch() {
        UUID branchId = UUID.randomUUID();
        BranchRequest request = new BranchRequest();
        request.setCode("BR-002");
        request.setOrgId(ORG_ID);
        Organization organization = new Organization();
        Branch branch = new Branch();
        branch.setCode("BR-001");
        branch.setOrgId(ORG_ID);
        Branch saved = new Branch();
        BranchResponse response = new BranchResponse();

        when(branchRepository.findByIdAndIsDeletedFalse(branchId)).thenReturn(Optional.of(branch));
        when(organizationRepository.findByIdAndIsDeletedFalse(ORG_ID)).thenReturn(Optional.of(organization));
        when(branchRepository.existsByCodeAndOrgIdAndIsDeletedFalse("BR-002", ORG_ID)).thenReturn(false);
        when(branchRepository.save(branch)).thenReturn(saved);
        when(branchMapper.toResponse(saved)).thenReturn(response);

        BranchResponse result = branchService.update(branchId, request);

        assertThat(result).isEqualTo(response);
        verify(branchMapper).updateEntity(request, branch);
    }

    @Test
    @DisplayName("delete should soft delete branch")
    void delete_shouldSoftDelete() {
        UUID branchId = UUID.randomUUID();
        Branch branch = new Branch();
        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken("admin", null));

        when(branchRepository.findByIdAndIsDeletedFalse(branchId)).thenReturn(Optional.of(branch));

        branchService.delete(branchId);

        assertThat(branch.getIsDeleted()).isTrue();
        assertThat(branch.getDeletedAt()).isNotNull();
        verify(branchRepository).save(branch);
    }

    @Test
    @DisplayName("activate should set isActive to true")
    void activate_shouldSetIsActiveTrue() {
        UUID branchId = UUID.randomUUID();
        Branch branch = new Branch();
        branch.setIsActive(false);
        Branch saved = new Branch();
        BranchResponse response = new BranchResponse();

        when(branchRepository.findByIdAndIsDeletedFalse(branchId)).thenReturn(Optional.of(branch));
        when(branchRepository.save(branch)).thenReturn(saved);
        when(branchMapper.toResponse(saved)).thenReturn(response);

        BranchResponse result = branchService.activate(branchId);

        assertThat(result).isEqualTo(response);
        assertThat(branch.getIsActive()).isTrue();
    }

    @Test
    @DisplayName("deactivate should set isActive to false")
    void deactivate_shouldSetIsActiveFalse() {
        UUID branchId = UUID.randomUUID();
        Branch branch = new Branch();
        branch.setIsActive(true);
        Branch saved = new Branch();
        BranchResponse response = new BranchResponse();

        when(branchRepository.findByIdAndIsDeletedFalse(branchId)).thenReturn(Optional.of(branch));
        when(branchRepository.save(branch)).thenReturn(saved);
        when(branchMapper.toResponse(saved)).thenReturn(response);

        BranchResponse result = branchService.deactivate(branchId);

        assertThat(result).isEqualTo(response);
        assertThat(branch.getIsActive()).isFalse();
    }
}
