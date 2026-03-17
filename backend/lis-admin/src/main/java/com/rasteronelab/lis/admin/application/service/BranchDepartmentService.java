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
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Service for BranchDepartment mapping CRUD operations.
 * Manages which departments are active per branch.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class BranchDepartmentService {

    private final BranchDepartmentRepository branchDepartmentRepository;
    private final BranchRepository branchRepository;
    private final DepartmentRepository departmentRepository;
    private final BranchDepartmentMapper branchDepartmentMapper;

    public BranchDepartmentResponse assign(BranchDepartmentRequest request) {
        Branch branch = branchRepository.findByIdAndIsDeletedFalse(request.getBranchId())
                .orElseThrow(() -> new NotFoundException("Branch", request.getBranchId()));

        Department department = departmentRepository.findByIdAndIsDeletedFalse(request.getDepartmentId())
                .orElseThrow(() -> new NotFoundException("Department", request.getDepartmentId()));

        if (branchDepartmentRepository.existsByBranchIdAndDepartmentIdAndIsDeletedFalse(
                request.getBranchId(), request.getDepartmentId())) {
            throw new DuplicateResourceException(
                    "Department is already assigned to this branch");
        }

        BranchDepartment mapping = new BranchDepartment();
        mapping.setBranch(branch);
        mapping.setDepartment(department);
        if (request.getIsActive() != null) {
            mapping.setIsActive(request.getIsActive());
        }

        BranchDepartment saved = branchDepartmentRepository.save(mapping);
        return branchDepartmentMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<BranchDepartmentResponse> getByBranch(UUID branchId) {
        if (!branchRepository.existsById(branchId)) {
            throw new NotFoundException("Branch", branchId);
        }
        return branchDepartmentRepository.findAllByBranchIdAndIsDeletedFalse(branchId)
                .stream()
                .map(branchDepartmentMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public BranchDepartmentResponse getById(UUID id) {
        BranchDepartment mapping = branchDepartmentRepository.findById(id)
                .filter(bd -> !bd.getIsDeleted())
                .orElseThrow(() -> new NotFoundException("BranchDepartment", id));
        return branchDepartmentMapper.toResponse(mapping);
    }

    public void unassign(UUID id) {
        BranchDepartment mapping = branchDepartmentRepository.findById(id)
                .filter(bd -> !bd.getIsDeleted())
                .orElseThrow(() -> new NotFoundException("BranchDepartment", id));

        String currentUser = SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : "system";

        mapping.softDelete(currentUser);
        branchDepartmentRepository.save(mapping);
    }
}
