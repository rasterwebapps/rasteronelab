package com.rasteronelab.lis.admin.application.service;

import com.rasteronelab.lis.admin.api.dto.InsuranceTariffRequest;
import com.rasteronelab.lis.admin.api.dto.InsuranceTariffResponse;
import com.rasteronelab.lis.admin.api.mapper.InsuranceTariffMapper;
import com.rasteronelab.lis.admin.domain.model.InsuranceTariff;
import com.rasteronelab.lis.admin.domain.repository.InsuranceTariffRepository;
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
 * Service for InsuranceTariff CRUD operations.
 * All queries are branch-scoped via BranchContextHolder.
 * InsuranceName + PlanName combination must be unique within a branch.
 */
@Service
@Transactional
public class InsuranceTariffService {

    private final InsuranceTariffRepository insuranceTariffRepository;
    private final InsuranceTariffMapper insuranceTariffMapper;

    public InsuranceTariffResponse create(InsuranceTariffRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        if (insuranceTariffRepository.existsByInsuranceNameAndPlanNameAndBranchIdAndIsDeletedFalse(
                request.getInsuranceName(), request.getPlanName(), branchId)) {
            throw new DuplicateResourceException("InsuranceTariff", "insuranceName+planName",
                    request.getInsuranceName() + "+" + request.getPlanName());
        }

        InsuranceTariff insuranceTariff = insuranceTariffMapper.toEntity(request);
        insuranceTariff.setBranchId(branchId);
        InsuranceTariff saved = insuranceTariffRepository.save(insuranceTariff);
        return insuranceTariffMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public InsuranceTariffResponse getById(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        InsuranceTariff insuranceTariff = insuranceTariffRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("InsuranceTariff", id));
        return insuranceTariffMapper.toResponse(insuranceTariff);
    }

    @Transactional(readOnly = true)
    public Page<InsuranceTariffResponse> getAll(Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return insuranceTariffRepository.findAllByBranchIdAndIsDeletedFalse(branchId, pageable)
                .map(insuranceTariffMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<InsuranceTariffResponse> search(String query, Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return insuranceTariffRepository.findAllByBranchIdAndIsDeletedFalseAndInsuranceNameContainingIgnoreCase(branchId, query, pageable)
                .map(insuranceTariffMapper::toResponse);
    }

    public InsuranceTariffResponse update(UUID id, InsuranceTariffRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        InsuranceTariff insuranceTariff = insuranceTariffRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("InsuranceTariff", id));

        // Validate unique insuranceName+planName within branch if combination changed
        if (!java.util.Objects.equals(request.getInsuranceName(), insuranceTariff.getInsuranceName())
                || !java.util.Objects.equals(request.getPlanName(), insuranceTariff.getPlanName())) {
            if (insuranceTariffRepository.existsByInsuranceNameAndPlanNameAndBranchIdAndIsDeletedFalse(
                    request.getInsuranceName(), request.getPlanName(), branchId)) {
                throw new DuplicateResourceException("InsuranceTariff", "insuranceName+planName",
                        request.getInsuranceName() + "+" + request.getPlanName());
            }
        }

        insuranceTariffMapper.updateEntity(request, insuranceTariff);
        InsuranceTariff saved = insuranceTariffRepository.save(insuranceTariff);
        return insuranceTariffMapper.toResponse(saved);
    }

    public void delete(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        InsuranceTariff insuranceTariff = insuranceTariffRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("InsuranceTariff", id));

        String currentUser = SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : "system";

        insuranceTariff.softDelete(currentUser);
        insuranceTariffRepository.save(insuranceTariff);
    }

    public InsuranceTariffService(InsuranceTariffRepository insuranceTariffRepository, InsuranceTariffMapper insuranceTariffMapper) {
        this.insuranceTariffRepository = insuranceTariffRepository;
        this.insuranceTariffMapper = insuranceTariffMapper;
    }

}
