package com.rasteronelab.lis.admin.application.service;

import com.rasteronelab.lis.admin.api.dto.DoctorRequest;
import com.rasteronelab.lis.admin.api.dto.DoctorResponse;
import com.rasteronelab.lis.admin.api.mapper.DoctorMapper;
import com.rasteronelab.lis.admin.domain.model.Doctor;
import com.rasteronelab.lis.admin.domain.repository.DoctorRepository;
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
 * Service for Doctor CRUD operations.
 * All queries are branch-scoped via BranchContextHolder.
 * Doctor code must be unique within a branch when provided.
 */
@Service
@Transactional
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;

    public DoctorResponse create(DoctorRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        if (request.getCode() != null && !request.getCode().isBlank()) {
            if (doctorRepository.existsByCodeAndBranchIdAndIsDeletedFalse(request.getCode(), branchId)) {
                throw new DuplicateResourceException("Doctor", "code", request.getCode());
            }
        }

        Doctor doctor = doctorMapper.toEntity(request);
        doctor.setBranchId(branchId);
        Doctor saved = doctorRepository.save(doctor);
        return doctorMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public DoctorResponse getById(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        Doctor doctor = doctorRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("Doctor", id));
        return doctorMapper.toResponse(doctor);
    }

    @Transactional(readOnly = true)
    public Page<DoctorResponse> getAll(Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return doctorRepository.findAllByBranchIdAndIsDeletedFalse(branchId, pageable)
                .map(doctorMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<DoctorResponse> search(String query, Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return doctorRepository.findAllByBranchIdAndIsDeletedFalseAndNameContainingIgnoreCase(branchId, query, pageable)
                .map(doctorMapper::toResponse);
    }

    public DoctorResponse update(UUID id, DoctorRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        Doctor doctor = doctorRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("Doctor", id));

        // Validate unique code within branch if code changed
        if (request.getCode() != null && !request.getCode().isBlank()
                && !request.getCode().equals(doctor.getCode())) {
            if (doctorRepository.existsByCodeAndBranchIdAndIsDeletedFalse(request.getCode(), branchId)) {
                throw new DuplicateResourceException("Doctor", "code", request.getCode());
            }
        }

        doctorMapper.updateEntity(request, doctor);
        Doctor saved = doctorRepository.save(doctor);
        return doctorMapper.toResponse(saved);
    }

    public void delete(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        Doctor doctor = doctorRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("Doctor", id));

        String currentUser = SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : "system";

        doctor.softDelete(currentUser);
        doctorRepository.save(doctor);
    }

    public DoctorService(DoctorRepository doctorRepository, DoctorMapper doctorMapper) {
        this.doctorRepository = doctorRepository;
        this.doctorMapper = doctorMapper;
    }

}
