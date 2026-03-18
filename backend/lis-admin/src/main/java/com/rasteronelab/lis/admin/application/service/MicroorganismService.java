package com.rasteronelab.lis.admin.application.service;

import com.rasteronelab.lis.admin.api.dto.MicroorganismRequest;
import com.rasteronelab.lis.admin.api.dto.MicroorganismResponse;
import com.rasteronelab.lis.admin.api.mapper.MicroorganismMapper;
import com.rasteronelab.lis.admin.domain.model.Microorganism;
import com.rasteronelab.lis.admin.domain.repository.MicroorganismRepository;
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
 * Service for Microorganism CRUD operations.
 * All queries are branch-scoped via BranchContextHolder.
 * Microorganism code must be unique within a branch.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class MicroorganismService {

    private final MicroorganismRepository microorganismRepository;
    private final MicroorganismMapper microorganismMapper;

    public MicroorganismResponse create(MicroorganismRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        if (microorganismRepository.existsByCodeAndBranchIdAndIsDeletedFalse(request.getCode(), branchId)) {
            throw new DuplicateResourceException("Microorganism", "code", request.getCode());
        }

        Microorganism microorganism = microorganismMapper.toEntity(request);
        microorganism.setBranchId(branchId);
        Microorganism saved = microorganismRepository.save(microorganism);
        return microorganismMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public MicroorganismResponse getById(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        Microorganism microorganism = microorganismRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("Microorganism", id));
        return microorganismMapper.toResponse(microorganism);
    }

    @Transactional(readOnly = true)
    public Page<MicroorganismResponse> getAll(Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return microorganismRepository.findAllByBranchIdAndIsDeletedFalse(branchId, pageable)
                .map(microorganismMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<MicroorganismResponse> search(String query, Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return microorganismRepository.findAllByBranchIdAndIsDeletedFalseAndNameContainingIgnoreCase(branchId, query, pageable)
                .map(microorganismMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<MicroorganismResponse> getByGramType(String gramType, Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return microorganismRepository.findAllByBranchIdAndGramTypeAndIsDeletedFalse(branchId, gramType, pageable)
                .map(microorganismMapper::toResponse);
    }

    public MicroorganismResponse update(UUID id, MicroorganismRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        Microorganism microorganism = microorganismRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("Microorganism", id));

        if (!request.getCode().equals(microorganism.getCode())) {
            if (microorganismRepository.existsByCodeAndBranchIdAndIsDeletedFalse(request.getCode(), branchId)) {
                throw new DuplicateResourceException("Microorganism", "code", request.getCode());
            }
        }

        microorganismMapper.updateEntity(request, microorganism);
        Microorganism saved = microorganismRepository.save(microorganism);
        return microorganismMapper.toResponse(saved);
    }

    public void delete(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        Microorganism microorganism = microorganismRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("Microorganism", id));

        String currentUser = SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : "system";

        microorganism.softDelete(currentUser);
        microorganismRepository.save(microorganism);
    }
}
