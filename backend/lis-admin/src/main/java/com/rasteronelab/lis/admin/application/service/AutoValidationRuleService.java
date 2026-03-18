package com.rasteronelab.lis.admin.application.service;

import com.rasteronelab.lis.admin.api.dto.AutoValidationRuleRequest;
import com.rasteronelab.lis.admin.api.dto.AutoValidationRuleResponse;
import com.rasteronelab.lis.admin.api.mapper.AutoValidationRuleMapper;
import com.rasteronelab.lis.admin.domain.model.AutoValidationRule;
import com.rasteronelab.lis.admin.domain.model.Parameter;
import com.rasteronelab.lis.admin.domain.model.TestMaster;
import com.rasteronelab.lis.admin.domain.repository.AutoValidationRuleRepository;
import com.rasteronelab.lis.admin.domain.repository.ParameterRepository;
import com.rasteronelab.lis.admin.domain.repository.TestMasterRepository;
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
 * Service for AutoValidationRule CRUD operations.
 * All queries are branch-scoped via BranchContextHolder.
 * Validates parameter and test existence when provided.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AutoValidationRuleService {

    private final AutoValidationRuleRepository autoValidationRuleRepository;
    private final ParameterRepository parameterRepository;
    private final TestMasterRepository testMasterRepository;
    private final AutoValidationRuleMapper autoValidationRuleMapper;

    public AutoValidationRuleResponse create(AutoValidationRuleRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        Parameter parameter = resolveParameter(request.getParameterId(), branchId);
        TestMaster test = resolveTest(request.getTestId(), branchId);

        AutoValidationRule rule = autoValidationRuleMapper.toEntity(request);
        rule.setBranchId(branchId);
        rule.setParameter(parameter);
        rule.setTest(test);
        AutoValidationRule saved = autoValidationRuleRepository.save(rule);
        return autoValidationRuleMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public AutoValidationRuleResponse getById(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        AutoValidationRule rule = autoValidationRuleRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("AutoValidationRule", id));
        return autoValidationRuleMapper.toResponse(rule);
    }

    @Transactional(readOnly = true)
    public Page<AutoValidationRuleResponse> getAll(Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return autoValidationRuleRepository.findAllByBranchIdAndIsDeletedFalse(branchId, pageable)
                .map(autoValidationRuleMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public List<AutoValidationRuleResponse> getByTestId(UUID testId) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        if (!testMasterRepository.existsByIdAndBranchIdAndIsDeletedFalse(testId, branchId)) {
            throw new NotFoundException("TestMaster", testId);
        }
        return autoValidationRuleRepository.findAllByBranchIdAndTestIdAndIsDeletedFalse(branchId, testId)
                .stream()
                .map(autoValidationRuleMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AutoValidationRuleResponse> getByParameterId(UUID parameterId) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        if (!parameterRepository.existsByIdAndBranchIdAndIsDeletedFalse(parameterId, branchId)) {
            throw new NotFoundException("Parameter", parameterId);
        }
        return autoValidationRuleRepository.findAllByBranchIdAndParameterIdAndIsDeletedFalse(branchId, parameterId)
                .stream()
                .map(autoValidationRuleMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AutoValidationRuleResponse> getByRuleType(String ruleType) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return autoValidationRuleRepository.findAllByBranchIdAndRuleTypeAndIsDeletedFalse(branchId, ruleType)
                .stream()
                .map(autoValidationRuleMapper::toResponse)
                .toList();
    }

    public AutoValidationRuleResponse update(UUID id, AutoValidationRuleRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        AutoValidationRule rule = autoValidationRuleRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("AutoValidationRule", id));

        Parameter parameter = resolveParameter(request.getParameterId(), branchId);
        TestMaster test = resolveTest(request.getTestId(), branchId);

        autoValidationRuleMapper.updateEntity(request, rule);
        rule.setParameter(parameter);
        rule.setTest(test);
        AutoValidationRule saved = autoValidationRuleRepository.save(rule);
        return autoValidationRuleMapper.toResponse(saved);
    }

    public void delete(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        AutoValidationRule rule = autoValidationRuleRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("AutoValidationRule", id));

        String currentUser = SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : "system";

        rule.softDelete(currentUser);
        autoValidationRuleRepository.save(rule);
    }

    private Parameter resolveParameter(UUID parameterId, UUID branchId) {
        if (parameterId == null) {
            return null;
        }
        return parameterRepository.findByIdAndBranchIdAndIsDeletedFalse(parameterId, branchId)
                .orElseThrow(() -> new NotFoundException("Parameter", parameterId));
    }

    private TestMaster resolveTest(UUID testId, UUID branchId) {
        if (testId == null) {
            return null;
        }
        return testMasterRepository.findByIdAndBranchIdAndIsDeletedFalse(testId, branchId)
                .orElseThrow(() -> new NotFoundException("TestMaster", testId));
    }
}
