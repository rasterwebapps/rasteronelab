package com.rasteronelab.lis.admin.application.service;

import com.rasteronelab.lis.admin.api.dto.ReferenceRangeRequest;
import com.rasteronelab.lis.admin.api.dto.ReferenceRangeResponse;
import com.rasteronelab.lis.admin.api.mapper.ReferenceRangeMapper;
import com.rasteronelab.lis.admin.domain.model.Parameter;
import com.rasteronelab.lis.admin.domain.model.ReferenceRange;
import com.rasteronelab.lis.admin.domain.repository.ParameterRepository;
import com.rasteronelab.lis.admin.domain.repository.ReferenceRangeRepository;
import com.rasteronelab.lis.core.common.exception.BusinessRuleException;
import com.rasteronelab.lis.core.common.exception.NotFoundException;
import com.rasteronelab.lis.core.infrastructure.BranchContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Service for ReferenceRange CRUD operations.
 * All queries are branch-scoped via BranchContextHolder.
 * Validates no overlapping age ranges for the same parameter and gender.
 */
@Service
@Transactional
public class ReferenceRangeService {

    private final ReferenceRangeRepository referenceRangeRepository;
    private final ParameterRepository parameterRepository;
    private final ReferenceRangeMapper referenceRangeMapper;

    public ReferenceRangeResponse create(ReferenceRangeRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        Parameter parameter = parameterRepository.findByIdAndBranchIdAndIsDeletedFalse(
                        request.getParameterId(), branchId)
                .orElseThrow(() -> new NotFoundException("Parameter", request.getParameterId()));

        validateNoOverlap(request, branchId, null);

        ReferenceRange range = referenceRangeMapper.toEntity(request);
        range.setBranchId(branchId);
        range.setParameter(parameter);
        ReferenceRange saved = referenceRangeRepository.save(range);
        return referenceRangeMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public ReferenceRangeResponse getById(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        ReferenceRange range = referenceRangeRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("ReferenceRange", id));
        return referenceRangeMapper.toResponse(range);
    }

    @Transactional(readOnly = true)
    public Page<ReferenceRangeResponse> getAll(Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return referenceRangeRepository.findAllByBranchIdAndIsDeletedFalse(branchId, pageable)
                .map(referenceRangeMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<ReferenceRangeResponse> getByParameterId(UUID parameterId) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        if (!parameterRepository.existsByIdAndBranchIdAndIsDeletedFalse(parameterId, branchId)) {
            throw new NotFoundException("Parameter", parameterId);
        }
        return referenceRangeRepository.findAllByParameterIdAndBranchIdAndIsDeletedFalse(parameterId, branchId)
                .stream()
                .map(referenceRangeMapper::toResponse)
                .toList();
    }

    public ReferenceRangeResponse update(UUID id, ReferenceRangeRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        ReferenceRange range = referenceRangeRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("ReferenceRange", id));

        Parameter parameter = parameterRepository.findByIdAndBranchIdAndIsDeletedFalse(
                        request.getParameterId(), branchId)
                .orElseThrow(() -> new NotFoundException("Parameter", request.getParameterId()));

        validateNoOverlap(request, branchId, id);

        referenceRangeMapper.updateEntity(request, range);
        range.setParameter(parameter);
        ReferenceRange saved = referenceRangeRepository.save(range);
        return referenceRangeMapper.toResponse(saved);
    }

    public void delete(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        ReferenceRange range = referenceRangeRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("ReferenceRange", id));

        String currentUser = SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : "system";

        range.softDelete(currentUser);
        referenceRangeRepository.save(range);
    }

    /**
     * Validates that the new/updated reference range does not overlap
     * with existing ranges for the same parameter and gender.
     */
    private void validateNoOverlap(ReferenceRangeRequest request, UUID branchId, UUID excludeId) {
        if (request.getAgeMin() == null || request.getAgeMax() == null) {
            return;
        }

        String gender = request.getGender();
        List<ReferenceRange> existing;
        if (gender != null) {
            existing = referenceRangeRepository.findAllByParameterIdAndGenderAndBranchIdAndIsDeletedFalse(
                    request.getParameterId(), gender, branchId);
        } else {
            existing = referenceRangeRepository.findAllByParameterIdAndBranchIdAndIsDeletedFalse(
                    request.getParameterId(), branchId);
        }

        for (ReferenceRange r : existing) {
            if (excludeId != null && excludeId.equals(r.getId())) {
                continue;
            }
            if (r.getAgeMin() == null || r.getAgeMax() == null) {
                continue;
            }
            if (rangesOverlap(request.getAgeMin(), request.getAgeMax(), r.getAgeMin(), r.getAgeMax())) {
                throw new BusinessRuleException(
                        "Reference range age overlap detected for parameter " + request.getParameterId()
                                + " and gender " + gender);
            }
        }
    }

    private boolean rangesOverlap(BigDecimal min1, BigDecimal max1, BigDecimal min2, BigDecimal max2) {
        return min1.compareTo(max2) <= 0 && min2.compareTo(max1) <= 0;
    }

    public ReferenceRangeService(ReferenceRangeRepository referenceRangeRepository, ParameterRepository parameterRepository, ReferenceRangeMapper referenceRangeMapper) {
        this.referenceRangeRepository = referenceRangeRepository;
        this.parameterRepository = parameterRepository;
        this.referenceRangeMapper = referenceRangeMapper;
    }

}
