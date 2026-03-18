package com.rasteronelab.lis.admin.application.service;

import com.rasteronelab.lis.admin.api.dto.OrganizationRequest;
import com.rasteronelab.lis.admin.api.dto.OrganizationResponse;
import com.rasteronelab.lis.admin.api.mapper.OrganizationMapper;
import com.rasteronelab.lis.admin.domain.model.Organization;
import com.rasteronelab.lis.admin.domain.repository.OrganizationRepository;
import com.rasteronelab.lis.core.common.exception.DuplicateResourceException;
import com.rasteronelab.lis.core.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Service for Organization CRUD operations.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationMapper organizationMapper;

    public OrganizationResponse create(OrganizationRequest request) {
        if (organizationRepository.existsByCodeAndIsDeletedFalse(request.getCode())) {
            throw new DuplicateResourceException("Organization", "code", request.getCode());
        }

        Organization organization = organizationMapper.toEntity(request);
        Organization saved = organizationRepository.save(organization);
        return organizationMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public OrganizationResponse getById(UUID id) {
        Organization organization = organizationRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("Organization", id));
        return organizationMapper.toResponse(organization);
    }

    @Transactional(readOnly = true)
    public Page<OrganizationResponse> getAll(Pageable pageable) {
        return organizationRepository.findAllByIsDeletedFalse(pageable)
                .map(organizationMapper::toResponse);
    }

    public OrganizationResponse update(UUID id, OrganizationRequest request) {
        Organization organization = organizationRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("Organization", id));

        // Validate unique code if it changed
        if (!organization.getCode().equals(request.getCode())
                && organizationRepository.existsByCodeAndIsDeletedFalse(request.getCode())) {
            throw new DuplicateResourceException("Organization", "code", request.getCode());
        }

        organizationMapper.updateEntity(request, organization);
        Organization saved = organizationRepository.save(organization);
        return organizationMapper.toResponse(saved);
    }

    public void delete(UUID id) {
        Organization organization = organizationRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("Organization", id));

        String currentUser = SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : "system";

        organization.softDelete(currentUser);
        organizationRepository.save(organization);
    }
}
