package com.rasteronelab.lis.admin.application.service;

import com.rasteronelab.lis.admin.api.dto.HolidayRequest;
import com.rasteronelab.lis.admin.api.dto.HolidayResponse;
import com.rasteronelab.lis.admin.api.mapper.HolidayMapper;
import com.rasteronelab.lis.admin.domain.model.Holiday;
import com.rasteronelab.lis.admin.domain.repository.HolidayRepository;
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
 * Service for Holiday CRUD operations.
 * All queries are branch-scoped via BranchContextHolder.
 * Holiday date must be unique within a branch.
 */
@Service
@Transactional
public class HolidayService {

    private final HolidayRepository holidayRepository;
    private final HolidayMapper holidayMapper;

    public HolidayResponse create(HolidayRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        if (holidayRepository.existsByBranchIdAndHolidayDateAndIsDeletedFalse(branchId, request.getHolidayDate())) {
            throw new DuplicateResourceException("Holiday", "holidayDate", request.getHolidayDate().toString());
        }

        Holiday holiday = holidayMapper.toEntity(request);
        holiday.setBranchId(branchId);
        Holiday saved = holidayRepository.save(holiday);
        return holidayMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public HolidayResponse getById(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        Holiday holiday = holidayRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("Holiday", id));
        return holidayMapper.toResponse(holiday);
    }

    @Transactional(readOnly = true)
    public Page<HolidayResponse> getAll(Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return holidayRepository.findAllByBranchIdAndIsDeletedFalse(branchId, pageable)
                .map(holidayMapper::toResponse);
    }

    public HolidayResponse update(UUID id, HolidayRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        Holiday holiday = holidayRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("Holiday", id));

        // Validate unique holiday date within branch if changed
        if (!request.getHolidayDate().equals(holiday.getHolidayDate())) {
            if (holidayRepository.existsByBranchIdAndHolidayDateAndIsDeletedFalse(branchId, request.getHolidayDate())) {
                throw new DuplicateResourceException("Holiday", "holidayDate", request.getHolidayDate().toString());
            }
        }

        holidayMapper.updateEntity(request, holiday);
        Holiday saved = holidayRepository.save(holiday);
        return holidayMapper.toResponse(saved);
    }

    public void delete(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        Holiday holiday = holidayRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("Holiday", id));

        String currentUser = SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : "system";

        holiday.softDelete(currentUser);
        holidayRepository.save(holiday);
    }

    public HolidayService(HolidayRepository holidayRepository, HolidayMapper holidayMapper) {
        this.holidayRepository = holidayRepository;
        this.holidayMapper = holidayMapper;
    }

}
