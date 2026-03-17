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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Service for Department CRUD operations.
 * Department code must be unique within an organization.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final OrganizationRepository organizationRepository;
    private final DepartmentMapper departmentMapper;

    public DepartmentResponse create(DepartmentRequest request) {
        Organization organization = organizationRepository.findByIdAndIsDeletedFalse(request.getOrgId())
                .orElseThrow(() -> new NotFoundException("Organization", request.getOrgId()));

        if (departmentRepository.existsByCodeAndOrgIdAndIsDeletedFalse(request.getCode(), request.getOrgId())) {
            throw new DuplicateResourceException("Department", "code", request.getCode());
        }

        Department department = departmentMapper.toEntity(request);
        department.setOrganization(organization);
        Department saved = departmentRepository.save(department);
        return departmentMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public DepartmentResponse getById(UUID id) {
        Department department = departmentRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("Department", id));
        return departmentMapper.toResponse(department);
    }

    @Transactional(readOnly = true)
    public Page<DepartmentResponse> getByOrganization(UUID orgId, Pageable pageable) {
        if (!organizationRepository.existsById(orgId)) {
            throw new NotFoundException("Organization", orgId);
        }
        return departmentRepository.findAllByOrgIdAndIsDeletedFalse(orgId, pageable)
                .map(departmentMapper::toResponse);
    }

    public DepartmentResponse update(UUID id, DepartmentRequest request) {
        Department department = departmentRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("Department", id));

        Organization organization = organizationRepository.findByIdAndIsDeletedFalse(request.getOrgId())
                .orElseThrow(() -> new NotFoundException("Organization", request.getOrgId()));

        // Validate unique code within org if code or org changed
        if (!request.getCode().equals(department.getCode())
                || !request.getOrgId().equals(department.getOrgId())) {
            if (departmentRepository.existsByCodeAndOrgIdAndIsDeletedFalse(request.getCode(), request.getOrgId())) {
                throw new DuplicateResourceException("Department", "code", request.getCode());
            }
        }

        departmentMapper.updateEntity(request, department);
        department.setOrganization(organization);
        Department saved = departmentRepository.save(department);
        return departmentMapper.toResponse(saved);
    }

    public void delete(UUID id) {
        Department department = departmentRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("Department", id));

        String currentUser = SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : "system";

        department.softDelete(currentUser);
        departmentRepository.save(department);
    }
}
