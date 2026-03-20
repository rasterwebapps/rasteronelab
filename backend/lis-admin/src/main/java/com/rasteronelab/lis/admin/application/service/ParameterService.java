package com.rasteronelab.lis.admin.application.service;

import com.rasteronelab.lis.admin.api.dto.ParameterRequest;
import com.rasteronelab.lis.admin.api.dto.ParameterResponse;
import com.rasteronelab.lis.admin.api.mapper.ParameterMapper;
import com.rasteronelab.lis.admin.domain.model.Parameter;
import com.rasteronelab.lis.admin.domain.repository.ParameterRepository;
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
 * Service for Parameter CRUD operations.
 * All queries are branch-scoped via BranchContextHolder.
 * Parameter code must be unique within a branch.
 */
@Service
@Transactional
public class ParameterService {

    private final ParameterRepository parameterRepository;
    private final ParameterMapper parameterMapper;

    public ParameterResponse create(ParameterRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        if (parameterRepository.existsByCodeAndBranchIdAndIsDeletedFalse(request.getCode(), branchId)) {
            throw new DuplicateResourceException("Parameter", "code", request.getCode());
        }

        Parameter parameter = parameterMapper.toEntity(request);
        parameter.setBranchId(branchId);
        Parameter saved = parameterRepository.save(parameter);
        return parameterMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public ParameterResponse getById(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        Parameter parameter = parameterRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("Parameter", id));
        return parameterMapper.toResponse(parameter);
    }

    @Transactional(readOnly = true)
    public Page<ParameterResponse> getAll(Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return parameterRepository.findAllByBranchIdAndIsDeletedFalse(branchId, pageable)
                .map(parameterMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<ParameterResponse> getActive(Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return parameterRepository.findAllByBranchIdAndIsActiveAndIsDeletedFalse(branchId, true, pageable)
                .map(parameterMapper::toResponse);
    }

    public ParameterResponse update(UUID id, ParameterRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        Parameter parameter = parameterRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("Parameter", id));

        if (!request.getCode().equals(parameter.getCode())) {
            if (parameterRepository.existsByCodeAndBranchIdAndIsDeletedFalse(request.getCode(), branchId)) {
                throw new DuplicateResourceException("Parameter", "code", request.getCode());
            }
        }

        parameterMapper.updateEntity(request, parameter);
        Parameter saved = parameterRepository.save(parameter);
        return parameterMapper.toResponse(saved);
    }

    public void delete(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        Parameter parameter = parameterRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("Parameter", id));

        String currentUser = SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : "system";

        parameter.softDelete(currentUser);
        parameterRepository.save(parameter);
    }

    public ParameterService(ParameterRepository parameterRepository, ParameterMapper parameterMapper) {
        this.parameterRepository = parameterRepository;
        this.parameterMapper = parameterMapper;
    }

}
