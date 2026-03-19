package com.rasteronelab.lis.patient.application.service;

import com.rasteronelab.lis.core.common.exception.NotFoundException;
import com.rasteronelab.lis.core.infrastructure.BranchContextHolder;
import com.rasteronelab.lis.patient.api.dto.DuplicateCheckRequest;
import com.rasteronelab.lis.patient.api.dto.PatientRequest;
import com.rasteronelab.lis.patient.api.dto.PatientResponse;
import com.rasteronelab.lis.patient.api.mapper.PatientMapper;
import com.rasteronelab.lis.patient.domain.model.Patient;
import com.rasteronelab.lis.patient.domain.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
    private final int duplicateScoreThreshold;

    public PatientService(PatientRepository patientRepository, PatientMapper patientMapper,
                          @Value("${lis.patient.duplicate-score-threshold:40}") int duplicateScoreThreshold) {
        this.patientRepository = patientRepository;
        this.patientMapper = patientMapper;
        this.duplicateScoreThreshold = duplicateScoreThreshold;
    }

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

    /**
     * Finds duplicate patients using a weighted scoring algorithm.
     * Scoring: name+DOB match = 40pts, phone match = 30pts, email match = 15pts, gender match = 15pts.
     * Patients scoring above the threshold (40 pts) are returned sorted by descending score.
     *
     * @return list of maps containing "patient" (PatientResponse) and "score" (int)
     */
    @Transactional(readOnly = true)
    public List<Map<String, Object>> findDuplicatesWithScore(DuplicateCheckRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        List<Patient> candidates = patientRepository.findDuplicates(branchId,
                request.getFirstName(), request.getLastName(),
                request.getPhone(), request.getDateOfBirth());

        List<Map<String, Object>> scored = new ArrayList<>();
        for (Patient candidate : candidates) {
            int score = calculateDuplicateScore(candidate, request.getFirstName(), request.getLastName(),
                    request.getPhone(), request.getEmail(), request.getGender(), request.getDateOfBirth());
            if (score >= duplicateScoreThreshold) {
                Map<String, Object> entry = new LinkedHashMap<>();
                entry.put("patient", patientMapper.toResponse(candidate));
                entry.put("score", score);
                scored.add(entry);
            }
        }

        scored.sort(Comparator.<Map<String, Object>, Integer>comparing(m -> (Integer) m.get("score")).reversed());
        return scored;
    }

    int calculateDuplicateScore(Patient candidate, String firstName, String lastName,
                                String phone, String email, String gender, LocalDate dob) {
        int score = 0;

        // Name + DOB match: 40 points
        boolean nameMatch = equalsIgnoreCaseNullSafe(candidate.getFirstName(), firstName)
                && equalsIgnoreCaseNullSafe(candidate.getLastName(), lastName);
        boolean dobMatch = candidate.getDateOfBirth() != null && candidate.getDateOfBirth().equals(dob);
        if (nameMatch && dobMatch) {
            score += 40;
        } else if (nameMatch) {
            score += 20;
        } else if (dobMatch) {
            score += 10;
        }

        // Phone match: 30 points
        if (phone != null && phone.equals(candidate.getPhone())) {
            score += 30;
        }

        // Email match: 15 points
        if (email != null && equalsIgnoreCaseNullSafe(candidate.getEmail(), email)) {
            score += 15;
        }

        // Gender match: 15 points
        if (gender != null && candidate.getGender() != null
                && gender.equalsIgnoreCase(candidate.getGender().name())) {
            score += 15;
        }

        return score;
    }

    private boolean equalsIgnoreCaseNullSafe(String a, String b) {
        if (a == null || b == null) {
            return false;
        }
        return a.equalsIgnoreCase(b);
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

}
