package com.rasteronelab.lis.admin.application.service;

import com.rasteronelab.lis.admin.api.dto.ReportTemplateRequest;
import com.rasteronelab.lis.admin.api.dto.ReportTemplateResponse;
import com.rasteronelab.lis.admin.api.mapper.ReportTemplateMapper;
import com.rasteronelab.lis.admin.domain.model.ReportTemplate;
import com.rasteronelab.lis.admin.domain.repository.ReportTemplateRepository;
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
 * Service for ReportTemplate CRUD operations.
 * All queries are branch-scoped via BranchContextHolder.
 * Template name must be unique within a branch.
 */
@Service
@Transactional
public class ReportTemplateService {

    private final ReportTemplateRepository reportTemplateRepository;
    private final ReportTemplateMapper reportTemplateMapper;

    public ReportTemplateResponse create(ReportTemplateRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        if (reportTemplateRepository.existsByTemplateNameAndBranchIdAndIsDeletedFalse(request.getTemplateName(), branchId)) {
            throw new DuplicateResourceException("ReportTemplate", "templateName", request.getTemplateName());
        }

        ReportTemplate reportTemplate = reportTemplateMapper.toEntity(request);
        reportTemplate.setBranchId(branchId);
        ReportTemplate saved = reportTemplateRepository.save(reportTemplate);
        return reportTemplateMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public ReportTemplateResponse getById(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        ReportTemplate reportTemplate = reportTemplateRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("ReportTemplate", id));
        return reportTemplateMapper.toResponse(reportTemplate);
    }

    @Transactional(readOnly = true)
    public Page<ReportTemplateResponse> getAll(Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return reportTemplateRepository.findAllByBranchIdAndIsDeletedFalse(branchId, pageable)
                .map(reportTemplateMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<ReportTemplateResponse> search(String query, Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return reportTemplateRepository.findAllByBranchIdAndIsDeletedFalseAndTemplateNameContainingIgnoreCase(branchId, query, pageable)
                .map(reportTemplateMapper::toResponse);
    }

    public ReportTemplateResponse update(UUID id, ReportTemplateRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        ReportTemplate reportTemplate = reportTemplateRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("ReportTemplate", id));

        // Validate unique template name within branch if name changed
        if (request.getTemplateName() != null && !request.getTemplateName().isBlank()
                && !request.getTemplateName().equals(reportTemplate.getTemplateName())) {
            if (reportTemplateRepository.existsByTemplateNameAndBranchIdAndIsDeletedFalse(request.getTemplateName(), branchId)) {
                throw new DuplicateResourceException("ReportTemplate", "templateName", request.getTemplateName());
            }
        }

        reportTemplateMapper.updateEntity(request, reportTemplate);
        ReportTemplate saved = reportTemplateRepository.save(reportTemplate);
        return reportTemplateMapper.toResponse(saved);
    }

    public void delete(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        ReportTemplate reportTemplate = reportTemplateRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("ReportTemplate", id));

        String currentUser = SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : "system";

        reportTemplate.softDelete(currentUser);
        reportTemplateRepository.save(reportTemplate);
    }

    public ReportTemplateService(ReportTemplateRepository reportTemplateRepository, ReportTemplateMapper reportTemplateMapper) {
        this.reportTemplateRepository = reportTemplateRepository;
        this.reportTemplateMapper = reportTemplateMapper;
    }

}
