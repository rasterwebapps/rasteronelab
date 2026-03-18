package com.rasteronelab.lis.admin.application.service;

import com.rasteronelab.lis.admin.api.dto.AntibioticRequest;
import com.rasteronelab.lis.admin.api.dto.AntibioticResponse;
import com.rasteronelab.lis.admin.api.mapper.AntibioticMapper;
import com.rasteronelab.lis.admin.domain.model.Antibiotic;
import com.rasteronelab.lis.admin.domain.repository.AntibioticRepository;
import com.rasteronelab.lis.core.common.exception.DuplicateResourceException;
import com.rasteronelab.lis.core.common.exception.NotFoundException;
import com.rasteronelab.lis.core.infrastructure.BranchContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Service for Antibiotic CRUD operations.
 * All queries are branch-scoped via BranchContextHolder.
 * Antibiotic code must be unique within a branch.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AntibioticService {

    private final AntibioticRepository antibioticRepository;
    private final AntibioticMapper antibioticMapper;

    public AntibioticResponse create(AntibioticRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        if (antibioticRepository.existsByCodeAndBranchIdAndIsDeletedFalse(request.getCode(), branchId)) {
            throw new DuplicateResourceException("Antibiotic", "code", request.getCode());
        }

        Antibiotic antibiotic = antibioticMapper.toEntity(request);
        antibiotic.setBranchId(branchId);
        Antibiotic saved = antibioticRepository.save(antibiotic);
        return antibioticMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public AntibioticResponse getById(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        Antibiotic antibiotic = antibioticRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("Antibiotic", id));
        return antibioticMapper.toResponse(antibiotic);
    }

    @Transactional(readOnly = true)
    public Page<AntibioticResponse> getAll(Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return antibioticRepository.findAllByBranchIdAndIsDeletedFalse(branchId, pageable)
                .map(antibioticMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<AntibioticResponse> search(String query, Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return antibioticRepository.findAllByBranchIdAndIsDeletedFalseAndNameContainingIgnoreCase(branchId, query, pageable)
                .map(antibioticMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<AntibioticResponse> getByAntibioticGroup(String antibioticGroup, Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return antibioticRepository.findAllByBranchIdAndAntibioticGroupAndIsDeletedFalse(branchId, antibioticGroup, pageable)
                .map(antibioticMapper::toResponse);
    }

    public AntibioticResponse update(UUID id, AntibioticRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        Antibiotic antibiotic = antibioticRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("Antibiotic", id));

        if (!request.getCode().equals(antibiotic.getCode())) {
            if (antibioticRepository.existsByCodeAndBranchIdAndIsDeletedFalse(request.getCode(), branchId)) {
                throw new DuplicateResourceException("Antibiotic", "code", request.getCode());
            }
        }

        antibioticMapper.updateEntity(request, antibiotic);
        Antibiotic saved = antibioticRepository.save(antibiotic);
        return antibioticMapper.toResponse(saved);
    }

    public void delete(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        Antibiotic antibiotic = antibioticRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("Antibiotic", id));

        String currentUser = SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : "system";

        antibiotic.softDelete(currentUser);
        antibioticRepository.save(antibiotic);
    }
}
