package com.rasteronelab.lis.admin.application.service;

import com.rasteronelab.lis.admin.api.dto.TestMasterRequest;
import com.rasteronelab.lis.admin.api.dto.TestMasterResponse;
import com.rasteronelab.lis.admin.api.mapper.TestMasterMapper;
import com.rasteronelab.lis.admin.domain.model.Department;
import com.rasteronelab.lis.admin.domain.model.TestMaster;
import com.rasteronelab.lis.admin.domain.repository.DepartmentRepository;
import com.rasteronelab.lis.admin.domain.repository.TestMasterRepository;
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
 * Service for TestMaster CRUD operations.
 * All queries are branch-scoped via BranchContextHolder.
 * Test code must be unique within a branch.
 */
@Service
@Transactional
public class TestMasterService {

    private final TestMasterRepository testMasterRepository;
    private final DepartmentRepository departmentRepository;
    private final TestMasterMapper testMasterMapper;

    public TestMasterResponse create(TestMasterRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        Department department = departmentRepository.findByIdAndIsDeletedFalse(request.getDepartmentId())
                .orElseThrow(() -> new NotFoundException("Department", request.getDepartmentId()));

        if (testMasterRepository.existsByCodeAndBranchIdAndIsDeletedFalse(request.getCode(), branchId)) {
            throw new DuplicateResourceException("Test", "code", request.getCode());
        }

        TestMaster testMaster = testMasterMapper.toEntity(request);
        testMaster.setBranchId(branchId);
        testMaster.setDepartment(department);
        TestMaster saved = testMasterRepository.save(testMaster);
        return testMasterMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public TestMasterResponse getById(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        TestMaster testMaster = testMasterRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("Test", id));
        return testMasterMapper.toResponse(testMaster);
    }

    @Transactional(readOnly = true)
    public Page<TestMasterResponse> getAll(Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return testMasterRepository.findAllByBranchIdAndIsDeletedFalse(branchId, pageable)
                .map(testMasterMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<TestMasterResponse> search(String query, Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return testMasterRepository.findAllByBranchIdAndIsDeletedFalseAndNameContainingIgnoreCase(branchId, query, pageable)
                .map(testMasterMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<TestMasterResponse> getByDepartment(UUID departmentId, Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        if (!departmentRepository.existsById(departmentId)) {
            throw new NotFoundException("Department", departmentId);
        }
        return testMasterRepository.findAllByBranchIdAndDepartmentIdAndIsDeletedFalse(branchId, departmentId, pageable)
                .map(testMasterMapper::toResponse);
    }

    public TestMasterResponse update(UUID id, TestMasterRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        TestMaster testMaster = testMasterRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("Test", id));

        Department department = departmentRepository.findByIdAndIsDeletedFalse(request.getDepartmentId())
                .orElseThrow(() -> new NotFoundException("Department", request.getDepartmentId()));

        // Validate unique code within branch if code changed
        if (!request.getCode().equals(testMaster.getCode())) {
            if (testMasterRepository.existsByCodeAndBranchIdAndIsDeletedFalse(request.getCode(), branchId)) {
                throw new DuplicateResourceException("Test", "code", request.getCode());
            }
        }

        testMasterMapper.updateEntity(request, testMaster);
        testMaster.setDepartment(department);
        TestMaster saved = testMasterRepository.save(testMaster);
        return testMasterMapper.toResponse(saved);
    }

    public void delete(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        TestMaster testMaster = testMasterRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("Test", id));

        String currentUser = SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : "system";

        testMaster.softDelete(currentUser);
        testMasterRepository.save(testMaster);
    }

    public TestMasterService(TestMasterRepository testMasterRepository, DepartmentRepository departmentRepository, TestMasterMapper testMasterMapper) {
        this.testMasterRepository = testMasterRepository;
        this.departmentRepository = departmentRepository;
        this.testMasterMapper = testMasterMapper;
    }

}
