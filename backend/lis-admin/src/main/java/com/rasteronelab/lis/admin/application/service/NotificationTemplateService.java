package com.rasteronelab.lis.admin.application.service;

import com.rasteronelab.lis.admin.api.dto.NotificationTemplateRequest;
import com.rasteronelab.lis.admin.api.dto.NotificationTemplateResponse;
import com.rasteronelab.lis.admin.api.mapper.NotificationTemplateMapper;
import com.rasteronelab.lis.admin.domain.model.NotificationTemplate;
import com.rasteronelab.lis.admin.domain.repository.NotificationTemplateRepository;
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
 * Service for NotificationTemplate CRUD operations.
 * All queries are branch-scoped via BranchContextHolder.
 * Template code must be unique within a branch.
 */
@Service
@Transactional
public class NotificationTemplateService {

    private final NotificationTemplateRepository notificationTemplateRepository;
    private final NotificationTemplateMapper notificationTemplateMapper;

    public NotificationTemplateResponse create(NotificationTemplateRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        if (notificationTemplateRepository.existsByTemplateCodeAndBranchIdAndIsDeletedFalse(request.getTemplateCode(), branchId)) {
            throw new DuplicateResourceException("NotificationTemplate", "templateCode", request.getTemplateCode());
        }

        NotificationTemplate template = notificationTemplateMapper.toEntity(request);
        template.setBranchId(branchId);
        NotificationTemplate saved = notificationTemplateRepository.save(template);
        return notificationTemplateMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public NotificationTemplateResponse getById(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        NotificationTemplate template = notificationTemplateRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("NotificationTemplate", id));
        return notificationTemplateMapper.toResponse(template);
    }

    @Transactional(readOnly = true)
    public Page<NotificationTemplateResponse> getAll(Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return notificationTemplateRepository.findAllByBranchIdAndIsDeletedFalse(branchId, pageable)
                .map(notificationTemplateMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<NotificationTemplateResponse> search(String query, Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return notificationTemplateRepository.findAllByBranchIdAndIsDeletedFalseAndTemplateNameContainingIgnoreCase(branchId, query, pageable)
                .map(notificationTemplateMapper::toResponse);
    }

    public NotificationTemplateResponse update(UUID id, NotificationTemplateRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        NotificationTemplate template = notificationTemplateRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("NotificationTemplate", id));

        // Validate unique templateCode within branch if code changed
        if (request.getTemplateCode() != null && !request.getTemplateCode().isBlank()
                && !request.getTemplateCode().equals(template.getTemplateCode())) {
            if (notificationTemplateRepository.existsByTemplateCodeAndBranchIdAndIsDeletedFalse(request.getTemplateCode(), branchId)) {
                throw new DuplicateResourceException("NotificationTemplate", "templateCode", request.getTemplateCode());
            }
        }

        notificationTemplateMapper.updateEntity(request, template);
        NotificationTemplate saved = notificationTemplateRepository.save(template);
        return notificationTemplateMapper.toResponse(saved);
    }

    public void delete(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        NotificationTemplate template = notificationTemplateRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("NotificationTemplate", id));

        String currentUser = SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : "system";

        template.softDelete(currentUser);
        notificationTemplateRepository.save(template);
    }

    public NotificationTemplateService(NotificationTemplateRepository notificationTemplateRepository, NotificationTemplateMapper notificationTemplateMapper) {
        this.notificationTemplateRepository = notificationTemplateRepository;
        this.notificationTemplateMapper = notificationTemplateMapper;
    }

}
