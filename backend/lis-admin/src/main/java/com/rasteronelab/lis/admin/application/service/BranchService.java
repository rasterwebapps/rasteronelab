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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Service for Branch CRUD operations.
 */
@Service
@Transactional
public class BranchService {

    private final BranchRepository branchRepository;
    private final OrganizationRepository organizationRepository;
    private final BranchMapper branchMapper;

    public BranchResponse create(BranchRequest request) {
        Organization organization = organizationRepository.findByIdAndIsDeletedFalse(request.getOrgId())
                .orElseThrow(() -> new NotFoundException("Organization", request.getOrgId()));

        if (branchRepository.existsByCodeAndOrgIdAndIsDeletedFalse(request.getCode(), request.getOrgId())) {
            throw new DuplicateResourceException("Branch", "code", request.getCode());
        }

        Branch branch = branchMapper.toEntity(request);
        branch.setOrganization(organization);
        Branch saved = branchRepository.save(branch);
        return branchMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public BranchResponse getById(UUID id) {
        Branch branch = branchRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("Branch", id));
        return branchMapper.toResponse(branch);
    }

    @Transactional(readOnly = true)
    public Page<BranchResponse> getAll(Pageable pageable) {
        return branchRepository.findAllByIsDeletedFalse(pageable)
                .map(branchMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<BranchResponse> getByOrganization(UUID orgId, Pageable pageable) {
        if (!organizationRepository.existsById(orgId)) {
            throw new NotFoundException("Organization", orgId);
        }
        return branchRepository.findAllByOrgIdAndIsDeletedFalse(orgId, pageable)
                .map(branchMapper::toResponse);
    }

    public BranchResponse update(UUID id, BranchRequest request) {
        Branch branch = branchRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("Branch", id));

        Organization organization = organizationRepository.findByIdAndIsDeletedFalse(request.getOrgId())
                .orElseThrow(() -> new NotFoundException("Organization", request.getOrgId()));

        // Validate unique code within org if code or org changed
        if (!request.getCode().equals(branch.getCode())
                || !request.getOrgId().equals(branch.getOrgId())) {
            if (branchRepository.existsByCodeAndOrgIdAndIsDeletedFalse(request.getCode(), request.getOrgId())) {
                throw new DuplicateResourceException("Branch", "code", request.getCode());
            }
        }

        branchMapper.updateEntity(request, branch);
        branch.setOrganization(organization);
        Branch saved = branchRepository.save(branch);
        return branchMapper.toResponse(saved);
    }

    public void delete(UUID id) {
        Branch branch = branchRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("Branch", id));

        String currentUser = SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : "system";

        branch.softDelete(currentUser);
        branchRepository.save(branch);
    }

    public BranchResponse activate(UUID id) {
        Branch branch = branchRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("Branch", id));
        branch.setIsActive(true);
        Branch saved = branchRepository.save(branch);
        return branchMapper.toResponse(saved);
    }

    public BranchResponse deactivate(UUID id) {
        Branch branch = branchRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("Branch", id));
        branch.setIsActive(false);
        Branch saved = branchRepository.save(branch);
        return branchMapper.toResponse(saved);
    }

    public BranchService(BranchRepository branchRepository, OrganizationRepository organizationRepository, BranchMapper branchMapper) {
        this.branchRepository = branchRepository;
        this.organizationRepository = organizationRepository;
        this.branchMapper = branchMapper;
    }

}
