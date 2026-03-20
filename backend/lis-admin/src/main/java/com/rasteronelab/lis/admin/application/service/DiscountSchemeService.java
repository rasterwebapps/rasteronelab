package com.rasteronelab.lis.admin.application.service;

import com.rasteronelab.lis.admin.api.dto.DiscountSchemeRequest;
import com.rasteronelab.lis.admin.api.dto.DiscountSchemeResponse;
import com.rasteronelab.lis.admin.api.mapper.DiscountSchemeMapper;
import com.rasteronelab.lis.admin.domain.model.DiscountScheme;
import com.rasteronelab.lis.admin.domain.repository.DiscountSchemeRepository;
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
 * Service for DiscountScheme CRUD operations.
 * All queries are branch-scoped via BranchContextHolder.
 * Scheme code must be unique within a branch.
 */
@Service
@Transactional
public class DiscountSchemeService {

    private final DiscountSchemeRepository discountSchemeRepository;
    private final DiscountSchemeMapper discountSchemeMapper;

    public DiscountSchemeResponse create(DiscountSchemeRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        if (discountSchemeRepository.existsBySchemeCodeAndBranchIdAndIsDeletedFalse(request.getSchemeCode(), branchId)) {
            throw new DuplicateResourceException("DiscountScheme", "schemeCode", request.getSchemeCode());
        }

        DiscountScheme discountScheme = discountSchemeMapper.toEntity(request);
        discountScheme.setBranchId(branchId);
        DiscountScheme saved = discountSchemeRepository.save(discountScheme);
        return discountSchemeMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public DiscountSchemeResponse getById(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        DiscountScheme discountScheme = discountSchemeRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("DiscountScheme", id));
        return discountSchemeMapper.toResponse(discountScheme);
    }

    @Transactional(readOnly = true)
    public Page<DiscountSchemeResponse> getAll(Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return discountSchemeRepository.findAllByBranchIdAndIsDeletedFalse(branchId, pageable)
                .map(discountSchemeMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<DiscountSchemeResponse> search(String query, Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return discountSchemeRepository.findAllByBranchIdAndIsDeletedFalseAndSchemeNameContainingIgnoreCase(branchId, query, pageable)
                .map(discountSchemeMapper::toResponse);
    }

    public DiscountSchemeResponse update(UUID id, DiscountSchemeRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        DiscountScheme discountScheme = discountSchemeRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("DiscountScheme", id));

        // Validate unique scheme code within branch if code changed
        if (!request.getSchemeCode().equals(discountScheme.getSchemeCode())) {
            if (discountSchemeRepository.existsBySchemeCodeAndBranchIdAndIsDeletedFalse(request.getSchemeCode(), branchId)) {
                throw new DuplicateResourceException("DiscountScheme", "schemeCode", request.getSchemeCode());
            }
        }

        discountSchemeMapper.updateEntity(request, discountScheme);
        DiscountScheme saved = discountSchemeRepository.save(discountScheme);
        return discountSchemeMapper.toResponse(saved);
    }

    public void delete(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        DiscountScheme discountScheme = discountSchemeRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("DiscountScheme", id));

        String currentUser = SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : "system";

        discountScheme.softDelete(currentUser);
        discountSchemeRepository.save(discountScheme);
    }

    public DiscountSchemeService(DiscountSchemeRepository discountSchemeRepository, DiscountSchemeMapper discountSchemeMapper) {
        this.discountSchemeRepository = discountSchemeRepository;
        this.discountSchemeMapper = discountSchemeMapper;
    }

}
