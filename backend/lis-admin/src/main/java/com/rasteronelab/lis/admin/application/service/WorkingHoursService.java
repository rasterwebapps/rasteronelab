package com.rasteronelab.lis.admin.application.service;

import com.rasteronelab.lis.admin.api.dto.WorkingHoursRequest;
import com.rasteronelab.lis.admin.api.dto.WorkingHoursResponse;
import com.rasteronelab.lis.admin.api.mapper.WorkingHoursMapper;
import com.rasteronelab.lis.admin.domain.model.WorkingHours;
import com.rasteronelab.lis.admin.domain.repository.WorkingHoursRepository;
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
 * Service for WorkingHours CRUD operations.
 * All queries are branch-scoped via BranchContextHolder.
 * Day of week must be unique within a branch. Open time must be before close time.
 */
@Service
@Transactional
public class WorkingHoursService {

    private final WorkingHoursRepository workingHoursRepository;
    private final WorkingHoursMapper workingHoursMapper;

    public WorkingHoursResponse create(WorkingHoursRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        if (workingHoursRepository.existsByBranchIdAndDayOfWeekAndIsDeletedFalse(branchId, request.getDayOfWeek())) {
            throw new DuplicateResourceException("WorkingHours", "dayOfWeek", String.valueOf(request.getDayOfWeek()));
        }

        validateOpenBeforeClose(request);

        WorkingHours workingHours = workingHoursMapper.toEntity(request);
        workingHours.setBranchId(branchId);
        WorkingHours saved = workingHoursRepository.save(workingHours);
        return workingHoursMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public WorkingHoursResponse getById(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        WorkingHours workingHours = workingHoursRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("WorkingHours", id));
        return workingHoursMapper.toResponse(workingHours);
    }

    @Transactional(readOnly = true)
    public Page<WorkingHoursResponse> getAll(Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return workingHoursRepository.findAllByBranchIdAndIsDeletedFalse(branchId, pageable)
                .map(workingHoursMapper::toResponse);
    }

    public WorkingHoursResponse update(UUID id, WorkingHoursRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        WorkingHours workingHours = workingHoursRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("WorkingHours", id));

        // Validate unique day of week within branch if changed
        if (!request.getDayOfWeek().equals(workingHours.getDayOfWeek())) {
            if (workingHoursRepository.existsByBranchIdAndDayOfWeekAndIsDeletedFalse(branchId, request.getDayOfWeek())) {
                throw new DuplicateResourceException("WorkingHours", "dayOfWeek", String.valueOf(request.getDayOfWeek()));
            }
        }

        validateOpenBeforeClose(request);

        workingHoursMapper.updateEntity(request, workingHours);
        WorkingHours saved = workingHoursRepository.save(workingHours);
        return workingHoursMapper.toResponse(saved);
    }

    public void delete(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        WorkingHours workingHours = workingHoursRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("WorkingHours", id));

        String currentUser = SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : "system";

        workingHours.softDelete(currentUser);
        workingHoursRepository.save(workingHours);
    }

    private void validateOpenBeforeClose(WorkingHoursRequest request) {
        if (request.getOpenTime() != null && request.getCloseTime() != null
                && !request.getOpenTime().isBefore(request.getCloseTime())) {
            throw new IllegalArgumentException("Open time must be before close time");
        }
    }

    public WorkingHoursService(WorkingHoursRepository workingHoursRepository, WorkingHoursMapper workingHoursMapper) {
        this.workingHoursRepository = workingHoursRepository;
        this.workingHoursMapper = workingHoursMapper;
    }

}
