package com.rasteronelab.lis.patient.application.service;

import com.rasteronelab.lis.core.common.exception.NotFoundException;
import com.rasteronelab.lis.core.infrastructure.BranchContextHolder;
import com.rasteronelab.lis.patient.api.dto.PatientRequest;
import com.rasteronelab.lis.patient.api.dto.PatientResponse;
import com.rasteronelab.lis.patient.api.mapper.PatientMapper;
import com.rasteronelab.lis.patient.domain.model.Patient;
import com.rasteronelab.lis.patient.domain.repository.PatientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for Patient CRUD operations.
 * All queries are branch-scoped via BranchContextHolder.
 * UHID is auto-generated per branch.
 */
@Service
@Transactional
public class PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    public PatientResponse create(PatientRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        Patient patient = patientMapper.toEntity(request);
        patient.setBranchId(branchId);
        patient.setIsActive(true);

        String uhid = generateUhid(branchId);
        patient.setUhid(uhid);

        Patient saved = patientRepository.save(patient);
        return patientMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public PatientResponse getById(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        Patient patient = patientRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("Patient", id));
        return patientMapper.toResponse(patient);
    }

    @Transactional(readOnly = true)
    public Page<PatientResponse> getAll(Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return patientRepository.findAllByBranchIdAndIsDeletedFalse(branchId, pageable)
                .map(patientMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<PatientResponse> search(String searchTerm, Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return patientRepository.searchPatients(branchId, searchTerm, pageable)
                .map(patientMapper::toResponse);
    }

    public PatientResponse update(UUID id, PatientRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        Patient patient = patientRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("Patient", id));

        patientMapper.updateEntity(request, patient);
        Patient saved = patientRepository.save(patient);
        return patientMapper.toResponse(saved);
    }

    public void delete(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        Patient patient = patientRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("Patient", id));

        String currentUser = SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : "system";

        patient.softDelete(currentUser);
        patientRepository.save(patient);
    }

    @Transactional(readOnly = true)
    public List<PatientResponse> findDuplicates(String firstName, String lastName,
                                                 String phone, LocalDate dob) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return patientRepository.findDuplicates(branchId, firstName, lastName, phone, dob)
                .stream()
                .map(patientMapper::toResponse)
                .collect(Collectors.toList());
    }

    private String generateUhid(UUID branchId) {
        long count = patientRepository.countByBranchIdAndIsDeletedFalse(branchId);
        long nextSequence = count + 1;
        String uhid = String.format("BR-%06d", nextSequence);

        // Ensure uniqueness in case of soft-deleted records reusing counts
        while (patientRepository.existsByUhidAndBranchIdAndIsDeletedFalse(uhid, branchId)) {
            nextSequence++;
            uhid = String.format("BR-%06d", nextSequence);
        }

        return uhid;
    }

    public PatientService(PatientRepository patientRepository, PatientMapper patientMapper) {
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
    }

}
