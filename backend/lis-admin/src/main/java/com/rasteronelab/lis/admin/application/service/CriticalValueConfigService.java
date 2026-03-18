package com.rasteronelab.lis.admin.application.service;

import com.rasteronelab.lis.admin.api.dto.CriticalValueConfigRequest;
import com.rasteronelab.lis.admin.api.dto.CriticalValueConfigResponse;
import com.rasteronelab.lis.admin.api.mapper.CriticalValueConfigMapper;
import com.rasteronelab.lis.admin.domain.model.CriticalValueConfig;
import com.rasteronelab.lis.admin.domain.model.Parameter;
import com.rasteronelab.lis.admin.domain.repository.CriticalValueConfigRepository;
import com.rasteronelab.lis.admin.domain.repository.ParameterRepository;
import com.rasteronelab.lis.core.common.exception.BusinessRuleException;
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
 * Service for CriticalValueConfig CRUD operations.
 * All queries are branch-scoped via BranchContextHolder.
 * Validates parameter existence and that low threshold is less than high threshold.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CriticalValueConfigService {

    private final CriticalValueConfigRepository criticalValueConfigRepository;
    private final ParameterRepository parameterRepository;
    private final CriticalValueConfigMapper criticalValueConfigMapper;

    public CriticalValueConfigResponse create(CriticalValueConfigRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        Parameter parameter = parameterRepository.findByIdAndBranchIdAndIsDeletedFalse(
                        request.getParameterId(), branchId)
                .orElseThrow(() -> new NotFoundException("Parameter", request.getParameterId()));

        validateThresholds(request);

        CriticalValueConfig config = criticalValueConfigMapper.toEntity(request);
        config.setBranchId(branchId);
        config.setParameter(parameter);
        CriticalValueConfig saved = criticalValueConfigRepository.save(config);
        return criticalValueConfigMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public CriticalValueConfigResponse getById(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        CriticalValueConfig config = criticalValueConfigRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("CriticalValueConfig", id));
        return criticalValueConfigMapper.toResponse(config);
    }

    @Transactional(readOnly = true)
    public Page<CriticalValueConfigResponse> getAll(Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return criticalValueConfigRepository.findAllByBranchIdAndIsDeletedFalse(branchId, pageable)
                .map(criticalValueConfigMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<CriticalValueConfigResponse> getByParameterId(UUID parameterId) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        if (!parameterRepository.existsByIdAndBranchIdAndIsDeletedFalse(parameterId, branchId)) {
            throw new NotFoundException("Parameter", parameterId);
        }
        return criticalValueConfigRepository.findAllByBranchIdAndParameterIdAndIsDeletedFalse(branchId, parameterId)
                .stream()
                .map(criticalValueConfigMapper::toResponse)
                .toList();
    }

    public CriticalValueConfigResponse update(UUID id, CriticalValueConfigRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        CriticalValueConfig config = criticalValueConfigRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("CriticalValueConfig", id));

        Parameter parameter = parameterRepository.findByIdAndBranchIdAndIsDeletedFalse(
                        request.getParameterId(), branchId)
                .orElseThrow(() -> new NotFoundException("Parameter", request.getParameterId()));

        validateThresholds(request);

        criticalValueConfigMapper.updateEntity(request, config);
        config.setParameter(parameter);
        CriticalValueConfig saved = criticalValueConfigRepository.save(config);
        return criticalValueConfigMapper.toResponse(saved);
    }

    public void delete(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        CriticalValueConfig config = criticalValueConfigRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("CriticalValueConfig", id));

        String currentUser = SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : "system";

        config.softDelete(currentUser);
        criticalValueConfigRepository.save(config);
    }

    /**
     * Validates that the low threshold is less than the high threshold
     * when both are provided.
     */
    private void validateThresholds(CriticalValueConfigRequest request) {
        if (request.getLowThreshold() != null && request.getHighThreshold() != null
                && request.getLowThreshold().compareTo(request.getHighThreshold()) >= 0) {
            throw new BusinessRuleException("Low threshold must be less than high threshold");
        }
    }
}
