package com.rasteronelab.lis.admin.application.service;

import com.rasteronelab.lis.admin.api.dto.OrganizationRequest;
import com.rasteronelab.lis.admin.api.dto.OrganizationResponse;
import com.rasteronelab.lis.admin.api.mapper.OrganizationMapper;
import com.rasteronelab.lis.admin.domain.model.Organization;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("OrganizationService")
@ExtendWith(MockitoExtension.class)
class OrganizationServiceTest {

    @Mock
    private OrganizationRepository organizationRepository;

    @Mock
    private OrganizationMapper organizationMapper;

    @InjectMocks
    private OrganizationService organizationService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("create should save and return organization")
    void create_shouldSaveAndReturnOrganization() {
        OrganizationRequest request = new OrganizationRequest();
        request.setCode("ORG-001");
        Organization organization = new Organization();
        Organization saved = new Organization();
        OrganizationResponse response = new OrganizationResponse();

        when(organizationRepository.existsByCodeAndIsDeletedFalse("ORG-001")).thenReturn(false);
        when(organizationMapper.toEntity(request)).thenReturn(organization);
        when(organizationRepository.save(organization)).thenReturn(saved);
        when(organizationMapper.toResponse(saved)).thenReturn(response);

        OrganizationResponse result = organizationService.create(request);

        assertThat(result).isEqualTo(response);
        verify(organizationRepository).save(organization);
    }

    @Test
    @DisplayName("create with duplicate code should throw DuplicateResourceException")
    void create_withDuplicateCode_shouldThrowDuplicateResourceException() {
        OrganizationRequest request = new OrganizationRequest();
        request.setCode("ORG-001");

        when(organizationRepository.existsByCodeAndIsDeletedFalse("ORG-001")).thenReturn(true);

        assertThatThrownBy(() -> organizationService.create(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("code");
    }

    @Test
    @DisplayName("getById should return organization")
    void getById_shouldReturnOrganization() {
        UUID orgId = UUID.randomUUID();
        Organization organization = new Organization();
        OrganizationResponse response = new OrganizationResponse();

        when(organizationRepository.findByIdAndIsDeletedFalse(orgId)).thenReturn(Optional.of(organization));
        when(organizationMapper.toResponse(organization)).thenReturn(response);

        OrganizationResponse result = organizationService.getById(orgId);

        assertThat(result).isEqualTo(response);
    }

    @Test
    @DisplayName("getById not found should throw NotFoundException")
    void getById_notFound_shouldThrowNotFoundException() {
        UUID orgId = UUID.randomUUID();

        when(organizationRepository.findByIdAndIsDeletedFalse(orgId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> organizationService.getById(orgId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Organization");
    }

    @Test
    @DisplayName("getAll should return paged results")
    void getAll_shouldReturnPagedResults() {
        Pageable pageable = PageRequest.of(0, 10);
        Organization organization = new Organization();
        OrganizationResponse response = new OrganizationResponse();
        Page<Organization> orgPage = new PageImpl<>(List.of(organization));

        when(organizationRepository.findAllByIsDeletedFalse(pageable)).thenReturn(orgPage);
        when(organizationMapper.toResponse(organization)).thenReturn(response);

        Page<OrganizationResponse> result = organizationService.getAll(pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst()).isEqualTo(response);
    }

    @Test
    @DisplayName("update should update and return organization")
    void update_shouldUpdateAndReturnOrganization() {
        UUID orgId = UUID.randomUUID();
        OrganizationRequest request = new OrganizationRequest();
        request.setCode("ORG-002");
        Organization organization = new Organization();
        organization.setCode("ORG-001");
        Organization saved = new Organization();
        OrganizationResponse response = new OrganizationResponse();

        when(organizationRepository.findByIdAndIsDeletedFalse(orgId)).thenReturn(Optional.of(organization));
        when(organizationRepository.existsByCodeAndIsDeletedFalse("ORG-002")).thenReturn(false);
        when(organizationRepository.save(organization)).thenReturn(saved);
        when(organizationMapper.toResponse(saved)).thenReturn(response);

        OrganizationResponse result = organizationService.update(orgId, request);

        assertThat(result).isEqualTo(response);
        verify(organizationMapper).updateEntity(request, organization);
    }

    @Test
    @DisplayName("delete should soft delete organization")
    void delete_shouldSoftDelete() {
        UUID orgId = UUID.randomUUID();
        Organization organization = new Organization();
        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken("admin", null));

        when(organizationRepository.findByIdAndIsDeletedFalse(orgId)).thenReturn(Optional.of(organization));

        organizationService.delete(orgId);

        assertThat(organization.getIsDeleted()).isTrue();
        assertThat(organization.getDeletedAt()).isNotNull();
        verify(organizationRepository).save(organization);
    }
}
