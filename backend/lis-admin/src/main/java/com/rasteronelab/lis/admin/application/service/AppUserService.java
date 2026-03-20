package com.rasteronelab.lis.admin.application.service;

import com.rasteronelab.lis.admin.api.dto.AppUserRequest;
import com.rasteronelab.lis.admin.api.dto.AppUserResponse;
import com.rasteronelab.lis.admin.api.mapper.AppUserMapper;
import com.rasteronelab.lis.admin.domain.model.AppUser;
import com.rasteronelab.lis.admin.domain.repository.AppUserRepository;
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
 * Service for AppUser CRUD operations.
 * All queries are branch-scoped via BranchContextHolder.
 * Username must be unique within a branch; keycloakUserId must be globally unique.
 */
@Service
@Transactional
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final AppUserMapper appUserMapper;

    public AppUserResponse create(AppUserRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        if (appUserRepository.existsByKeycloakUserIdAndIsDeletedFalse(request.getKeycloakUserId())) {
            throw new DuplicateResourceException("User", "keycloakUserId", request.getKeycloakUserId());
        }

        if (appUserRepository.existsByUsernameAndBranchIdAndIsDeletedFalse(request.getUsername(), branchId)) {
            throw new DuplicateResourceException("User", "username", request.getUsername());
        }

        AppUser appUser = appUserMapper.toEntity(request);
        appUser.setBranchId(branchId);
        AppUser saved = appUserRepository.save(appUser);
        return appUserMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public AppUserResponse getById(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        AppUser appUser = appUserRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("User", id));
        return appUserMapper.toResponse(appUser);
    }

    @Transactional(readOnly = true)
    public Page<AppUserResponse> getAll(Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return appUserRepository.findAllByBranchIdAndIsDeletedFalse(branchId, pageable)
                .map(appUserMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<AppUserResponse> search(String query, Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return appUserRepository.findAllByBranchIdAndIsDeletedFalseAndUsernameContainingIgnoreCase(branchId, query, pageable)
                .map(appUserMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<AppUserResponse> getByActiveStatus(Boolean isActive, Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return appUserRepository.findAllByBranchIdAndIsActiveAndIsDeletedFalse(branchId, isActive, pageable)
                .map(appUserMapper::toResponse);
    }

    public AppUserResponse update(UUID id, AppUserRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        AppUser appUser = appUserRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("User", id));

        // Validate unique keycloakUserId globally if changed
        if (!request.getKeycloakUserId().equals(appUser.getKeycloakUserId())) {
            if (appUserRepository.existsByKeycloakUserIdAndIsDeletedFalse(request.getKeycloakUserId())) {
                throw new DuplicateResourceException("User", "keycloakUserId", request.getKeycloakUserId());
            }
        }

        // Validate unique username within branch if changed
        if (!request.getUsername().equals(appUser.getUsername())) {
            if (appUserRepository.existsByUsernameAndBranchIdAndIsDeletedFalse(request.getUsername(), branchId)) {
                throw new DuplicateResourceException("User", "username", request.getUsername());
            }
        }

        appUserMapper.updateEntity(request, appUser);
        AppUser saved = appUserRepository.save(appUser);
        return appUserMapper.toResponse(saved);
    }

    public void delete(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        AppUser appUser = appUserRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("User", id));

        String currentUser = SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : "system";

        appUser.softDelete(currentUser);
        appUserRepository.save(appUser);
    }

    public AppUserService(AppUserRepository appUserRepository, AppUserMapper appUserMapper) {
        this.appUserRepository = appUserRepository;
        this.appUserMapper = appUserMapper;
    }

}
