package com.rasteronelab.lis.patient.application.service;

import com.rasteronelab.lis.core.common.exception.NotFoundException;
import com.rasteronelab.lis.core.infrastructure.BranchContextHolder;
import com.rasteronelab.lis.patient.api.dto.PatientVisitRequest;
import com.rasteronelab.lis.patient.api.dto.PatientVisitResponse;
import com.rasteronelab.lis.patient.api.mapper.PatientVisitMapper;
import com.rasteronelab.lis.patient.domain.model.Patient;
import com.rasteronelab.lis.patient.domain.model.PatientVisit;
import com.rasteronelab.lis.patient.domain.repository.PatientRepository;
import com.rasteronelab.lis.patient.domain.repository.PatientVisitRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Service for PatientVisit CRUD operations.
 * All queries are branch-scoped via BranchContextHolder.
 * Visit number is auto-generated per branch.
 */
@Service
@Transactional
public class PatientVisitService {

    private final PatientVisitRepository patientVisitRepository;
    private final PatientRepository patientRepository;
    private final PatientVisitMapper patientVisitMapper;

    public PatientVisitResponse create(UUID patientId, PatientVisitRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        Patient patient = patientRepository.findByIdAndBranchIdAndIsDeletedFalse(patientId, branchId)
                .orElseThrow(() -> new NotFoundException("Patient", patientId));

        PatientVisit visit = patientVisitMapper.toEntity(request);
        visit.setBranchId(branchId);
        visit.setPatient(patient);
        visit.setVisitDate(LocalDateTime.now());
        visit.setIsActive(true);

        String visitNumber = generateVisitNumber(branchId);
        visit.setVisitNumber(visitNumber);

        PatientVisit saved = patientVisitRepository.save(visit);
        return patientVisitMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public PatientVisitResponse getById(UUID patientId, UUID visitId) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        if (!patientRepository.existsByIdAndBranchIdAndIsDeletedFalse(patientId, branchId)) {
            throw new NotFoundException("Patient", patientId);
        }

        PatientVisit visit = patientVisitRepository.findByIdAndBranchIdAndIsDeletedFalse(visitId, branchId)
                .orElseThrow(() -> new NotFoundException("PatientVisit", visitId));
        return patientVisitMapper.toResponse(visit);
    }

    @Transactional(readOnly = true)
    public Page<PatientVisitResponse> getByPatientId(UUID patientId, Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        if (!patientRepository.existsByIdAndBranchIdAndIsDeletedFalse(patientId, branchId)) {
            throw new NotFoundException("Patient", patientId);
        }

        return patientVisitRepository.findAllByPatientIdAndBranchIdAndIsDeletedFalse(
                        patientId, branchId, pageable)
                .map(patientVisitMapper::toResponse);
    }

    public PatientVisitResponse update(UUID patientId, UUID visitId, PatientVisitRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        if (!patientRepository.existsByIdAndBranchIdAndIsDeletedFalse(patientId, branchId)) {
            throw new NotFoundException("Patient", patientId);
        }

        PatientVisit visit = patientVisitRepository.findByIdAndBranchIdAndIsDeletedFalse(visitId, branchId)
                .orElseThrow(() -> new NotFoundException("PatientVisit", visitId));

        patientVisitMapper.updateEntity(request, visit);
        PatientVisit saved = patientVisitRepository.save(visit);
        return patientVisitMapper.toResponse(saved);
    }

    public void delete(UUID patientId, UUID visitId) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        if (!patientRepository.existsByIdAndBranchIdAndIsDeletedFalse(patientId, branchId)) {
            throw new NotFoundException("Patient", patientId);
        }

        PatientVisit visit = patientVisitRepository.findByIdAndBranchIdAndIsDeletedFalse(visitId, branchId)
                .orElseThrow(() -> new NotFoundException("PatientVisit", visitId));

        String currentUser = SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : "system";

        visit.softDelete(currentUser);
        patientVisitRepository.save(visit);
    }

    private String generateVisitNumber(UUID branchId) {
        long count = patientVisitRepository.countByBranchIdAndIsDeletedFalse(branchId);
        long nextSequence = count + 1;
        String visitNumber = String.format("VIS-%06d", nextSequence);

        while (patientVisitRepository.existsByVisitNumberAndBranchIdAndIsDeletedFalse(visitNumber, branchId)) {
            nextSequence++;
            visitNumber = String.format("VIS-%06d", nextSequence);
        }

        return visitNumber;
    }

    public PatientVisitService(PatientVisitRepository patientVisitRepository,
                               PatientRepository patientRepository,
                               PatientVisitMapper patientVisitMapper) {
        this.patientVisitRepository = patientVisitRepository;
        this.patientRepository = patientRepository;
        this.patientVisitMapper = patientVisitMapper;
    }

}
