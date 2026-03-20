package com.rasteronelab.lis.admin.application.service;

import com.rasteronelab.lis.admin.api.dto.TATConfigurationRequest;
import com.rasteronelab.lis.admin.api.dto.TATConfigurationResponse;
import com.rasteronelab.lis.admin.api.mapper.TATConfigurationMapper;
import com.rasteronelab.lis.admin.domain.model.Department;
import com.rasteronelab.lis.admin.domain.model.TATConfiguration;
import com.rasteronelab.lis.admin.domain.model.TestMaster;
import com.rasteronelab.lis.admin.domain.repository.DepartmentRepository;
import com.rasteronelab.lis.admin.domain.repository.TATConfigurationRepository;
import com.rasteronelab.lis.admin.domain.repository.TestMasterRepository;
import com.rasteronelab.lis.core.common.exception.NotFoundException;
import com.rasteronelab.lis.core.infrastructure.BranchContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Service for TATConfiguration CRUD operations.
 * All queries are branch-scoped via BranchContextHolder.
 * Validates test and department existence on create/update.
 */
@Service
@Transactional
public class TATConfigurationService {

    private final TATConfigurationRepository tatConfigurationRepository;
    private final TestMasterRepository testMasterRepository;
    private final DepartmentRepository departmentRepository;
    private final TATConfigurationMapper tatConfigurationMapper;

    public TATConfigurationResponse create(TATConfigurationRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        TATConfiguration tatConfiguration = tatConfigurationMapper.toEntity(request);
        tatConfiguration.setBranchId(branchId);

        if (request.getTestId() != null) {
            TestMaster test = testMasterRepository.findByIdAndBranchIdAndIsDeletedFalse(request.getTestId(), branchId)
                    .orElseThrow(() -> new NotFoundException("Test", request.getTestId()));
            tatConfiguration.setTest(test);
        }

        if (request.getDepartmentId() != null) {
            Department department = departmentRepository.findByIdAndIsDeletedFalse(request.getDepartmentId())
                    .orElseThrow(() -> new NotFoundException("Department", request.getDepartmentId()));
            tatConfiguration.setDepartment(department);
        }

        TATConfiguration saved = tatConfigurationRepository.save(tatConfiguration);
        return tatConfigurationMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public TATConfigurationResponse getById(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        TATConfiguration tatConfiguration = tatConfigurationRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("TATConfiguration", id));
        return tatConfigurationMapper.toResponse(tatConfiguration);
    }

    @Transactional(readOnly = true)
    public Page<TATConfigurationResponse> getAll(Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return tatConfigurationRepository.findAllByBranchIdAndIsDeletedFalse(branchId, pageable)
                .map(tatConfigurationMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<TATConfigurationResponse> getByTest(UUID testId, Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        testMasterRepository.findByIdAndBranchIdAndIsDeletedFalse(testId, branchId)
                .orElseThrow(() -> new NotFoundException("Test", testId));
        return tatConfigurationRepository.findAllByBranchIdAndTestIdAndIsDeletedFalse(branchId, testId, pageable)
                .map(tatConfigurationMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<TATConfigurationResponse> getByDepartment(UUID departmentId, Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        departmentRepository.findByIdAndIsDeletedFalse(departmentId)
                .orElseThrow(() -> new NotFoundException("Department", departmentId));
        return tatConfigurationRepository.findAllByBranchIdAndDepartmentIdAndIsDeletedFalse(branchId, departmentId, pageable)
                .map(tatConfigurationMapper::toResponse);
    }

    public TATConfigurationResponse update(UUID id, TATConfigurationRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        TATConfiguration tatConfiguration = tatConfigurationRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("TATConfiguration", id));

        if (request.getTestId() != null) {
            TestMaster test = testMasterRepository.findByIdAndBranchIdAndIsDeletedFalse(request.getTestId(), branchId)
                    .orElseThrow(() -> new NotFoundException("Test", request.getTestId()));
            tatConfiguration.setTest(test);
        } else {
            tatConfiguration.setTest(null);
        }

        if (request.getDepartmentId() != null) {
            Department department = departmentRepository.findByIdAndIsDeletedFalse(request.getDepartmentId())
                    .orElseThrow(() -> new NotFoundException("Department", request.getDepartmentId()));
            tatConfiguration.setDepartment(department);
        } else {
            tatConfiguration.setDepartment(null);
        }

        tatConfigurationMapper.updateEntity(request, tatConfiguration);
        TATConfiguration saved = tatConfigurationRepository.save(tatConfiguration);
        return tatConfigurationMapper.toResponse(saved);
    }

    public void delete(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        TATConfiguration tatConfiguration = tatConfigurationRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("TATConfiguration", id));

        String currentUser = SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : "system";

        tatConfiguration.softDelete(currentUser);
        tatConfigurationRepository.save(tatConfiguration);
    }

    public TATConfigurationService(TATConfigurationRepository tatConfigurationRepository, TestMasterRepository testMasterRepository, DepartmentRepository departmentRepository, TATConfigurationMapper tatConfigurationMapper) {
        this.tatConfigurationRepository = tatConfigurationRepository;
        this.testMasterRepository = testMasterRepository;
        this.departmentRepository = departmentRepository;
        this.tatConfigurationMapper = tatConfigurationMapper;
    }

}
