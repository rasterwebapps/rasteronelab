package com.rasteronelab.lis.admin.application.service;

import com.rasteronelab.lis.admin.api.dto.NumberSeriesRequest;
import com.rasteronelab.lis.admin.api.dto.NumberSeriesResponse;
import com.rasteronelab.lis.admin.api.mapper.NumberSeriesMapper;
import com.rasteronelab.lis.admin.domain.model.NumberSeries;
import com.rasteronelab.lis.admin.domain.repository.NumberSeriesRepository;
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
 * Service for NumberSeries CRUD operations.
 * All queries are branch-scoped via BranchContextHolder.
 * Entity type must be unique within a branch.
 */
@Service
@Transactional
public class NumberSeriesService {

    private final NumberSeriesRepository numberSeriesRepository;
    private final NumberSeriesMapper numberSeriesMapper;

    public NumberSeriesResponse create(NumberSeriesRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        if (numberSeriesRepository.existsByEntityTypeAndBranchIdAndIsDeletedFalse(request.getEntityType(), branchId)) {
            throw new DuplicateResourceException("NumberSeries", "entityType", request.getEntityType());
        }

        NumberSeries numberSeries = numberSeriesMapper.toEntity(request);
        numberSeries.setBranchId(branchId);
        NumberSeries saved = numberSeriesRepository.save(numberSeries);
        return numberSeriesMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public NumberSeriesResponse getById(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        NumberSeries numberSeries = numberSeriesRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("NumberSeries", id));
        return numberSeriesMapper.toResponse(numberSeries);
    }

    @Transactional(readOnly = true)
    public Page<NumberSeriesResponse> getAll(Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return numberSeriesRepository.findAllByBranchIdAndIsDeletedFalse(branchId, pageable)
                .map(numberSeriesMapper::toResponse);
    }

    public NumberSeriesResponse update(UUID id, NumberSeriesRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        NumberSeries numberSeries = numberSeriesRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("NumberSeries", id));

        // Validate unique entity type within branch if changed
        if (!request.getEntityType().equals(numberSeries.getEntityType())) {
            if (numberSeriesRepository.existsByEntityTypeAndBranchIdAndIsDeletedFalse(request.getEntityType(), branchId)) {
                throw new DuplicateResourceException("NumberSeries", "entityType", request.getEntityType());
            }
        }

        numberSeriesMapper.updateEntity(request, numberSeries);
        NumberSeries saved = numberSeriesRepository.save(numberSeries);
        return numberSeriesMapper.toResponse(saved);
    }

    public void delete(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        NumberSeries numberSeries = numberSeriesRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("NumberSeries", id));

        String currentUser = SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : "system";

        numberSeries.softDelete(currentUser);
        numberSeriesRepository.save(numberSeries);
    }

    public NumberSeriesService(NumberSeriesRepository numberSeriesRepository, NumberSeriesMapper numberSeriesMapper) {
        this.numberSeriesRepository = numberSeriesRepository;
        this.numberSeriesMapper = numberSeriesMapper;
    }

}
