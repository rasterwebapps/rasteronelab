package com.rasteronelab.lis.admin.application.service;

import com.rasteronelab.lis.admin.api.dto.RoleRequest;
import com.rasteronelab.lis.admin.api.dto.RoleResponse;
import com.rasteronelab.lis.admin.api.mapper.RoleMapper;
import com.rasteronelab.lis.admin.domain.model.Role;
import com.rasteronelab.lis.admin.domain.repository.RoleRepository;
import com.rasteronelab.lis.core.common.exception.DuplicateResourceException;
import com.rasteronelab.lis.core.common.exception.NotFoundException;
import com.rasteronelab.lis.core.infrastructure.BranchContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Service for Role CRUD operations.
 * All queries are branch-scoped via BranchContextHolder.
 * Role name must be unique within a branch.
 */
@Service
@Transactional
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleResponse create(RoleRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        if (roleRepository.existsByRoleNameAndBranchIdAndIsDeletedFalse(request.getRoleName(), branchId)) {
            throw new DuplicateResourceException("Role", "roleName", request.getRoleName());
        }

        Role role = roleMapper.toEntity(request);
        role.setBranchId(branchId);
        Role saved = roleRepository.save(role);
        return roleMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public RoleResponse getById(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        Role role = roleRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("Role", id));
        return roleMapper.toResponse(role);
    }

    @Transactional(readOnly = true)
    public Page<RoleResponse> getAll(Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return roleRepository.findAllByBranchIdAndIsDeletedFalse(branchId, pageable)
                .map(roleMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<RoleResponse> search(String query, Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return roleRepository.findAllByBranchIdAndIsDeletedFalseAndRoleNameContainingIgnoreCase(branchId, query, pageable)
                .map(roleMapper::toResponse);
    }

    public RoleResponse update(UUID id, RoleRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        Role role = roleRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("Role", id));

        // Validate unique role name within branch if name changed
        if (request.getRoleName() != null && !request.getRoleName().isBlank()
                && !request.getRoleName().equals(role.getRoleName())) {
            if (roleRepository.existsByRoleNameAndBranchIdAndIsDeletedFalse(request.getRoleName(), branchId)) {
                throw new DuplicateResourceException("Role", "roleName", request.getRoleName());
            }
        }

        roleMapper.updateEntity(request, role);
        Role saved = roleRepository.save(role);
        return roleMapper.toResponse(saved);
    }

    public void delete(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        Role role = roleRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("Role", id));

        String currentUser = SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : "system";

        role.softDelete(currentUser);
        roleRepository.save(role);
    }

    public RoleService(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

}
