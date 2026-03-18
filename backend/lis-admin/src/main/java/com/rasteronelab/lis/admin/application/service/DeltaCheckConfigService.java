package com.rasteronelab.lis.admin.application.service;

import com.rasteronelab.lis.admin.api.dto.DeltaCheckConfigRequest;
import com.rasteronelab.lis.admin.api.dto.DeltaCheckConfigResponse;
import com.rasteronelab.lis.admin.api.mapper.DeltaCheckConfigMapper;
import com.rasteronelab.lis.admin.domain.model.DeltaCheckConfig;
import com.rasteronelab.lis.admin.domain.model.Parameter;
import com.rasteronelab.lis.admin.domain.repository.DeltaCheckConfigRepository;
import com.rasteronelab.lis.admin.domain.repository.ParameterRepository;
import com.rasteronelab.lis.core.common.exception.NotFoundException;
import com.rasteronelab.lis.core.infrastructure.BranchContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Service for DeltaCheckConfig CRUD operations.
 * All queries are branch-scoped via BranchContextHolder.
 * Validates parameter existence before creating or updating configurations.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class DeltaCheckConfigService {

    private final DeltaCheckConfigRepository deltaCheckConfigRepository;
    private final ParameterRepository parameterRepository;
    private final DeltaCheckConfigMapper deltaCheckConfigMapper;

    public DeltaCheckConfigResponse create(DeltaCheckConfigRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        Parameter parameter = parameterRepository.findByIdAndBranchIdAndIsDeletedFalse(
                        request.getParameterId(), branchId)
                .orElseThrow(() -> new NotFoundException("Parameter", request.getParameterId()));

        DeltaCheckConfig config = deltaCheckConfigMapper.toEntity(request);
        config.setBranchId(branchId);
        config.setParameter(parameter);
        DeltaCheckConfig saved = deltaCheckConfigRepository.save(config);
        return deltaCheckConfigMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public DeltaCheckConfigResponse getById(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        DeltaCheckConfig config = deltaCheckConfigRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("DeltaCheckConfig", id));
        return deltaCheckConfigMapper.toResponse(config);
    }

    @Transactional(readOnly = true)
    public Page<DeltaCheckConfigResponse> getAll(Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return deltaCheckConfigRepository.findAllByBranchIdAndIsDeletedFalse(branchId, pageable)
                .map(deltaCheckConfigMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<DeltaCheckConfigResponse> getByParameterId(UUID parameterId) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        if (!parameterRepository.existsByIdAndBranchIdAndIsDeletedFalse(parameterId, branchId)) {
            throw new NotFoundException("Parameter", parameterId);
        }
        return deltaCheckConfigRepository.findAllByBranchIdAndParameterIdAndIsDeletedFalse(branchId, parameterId)
                .stream()
                .map(deltaCheckConfigMapper::toResponse)
                .toList();
    }

    public DeltaCheckConfigResponse update(UUID id, DeltaCheckConfigRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        DeltaCheckConfig config = deltaCheckConfigRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("DeltaCheckConfig", id));

        Parameter parameter = parameterRepository.findByIdAndBranchIdAndIsDeletedFalse(
                        request.getParameterId(), branchId)
                .orElseThrow(() -> new NotFoundException("Parameter", request.getParameterId()));

        deltaCheckConfigMapper.updateEntity(request, config);
        config.setParameter(parameter);
        DeltaCheckConfig saved = deltaCheckConfigRepository.save(config);
        return deltaCheckConfigMapper.toResponse(saved);
    }

    public void delete(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        DeltaCheckConfig config = deltaCheckConfigRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("DeltaCheckConfig", id));

        String currentUser = SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : "system";

        config.softDelete(currentUser);
        deltaCheckConfigRepository.save(config);
    }
}
