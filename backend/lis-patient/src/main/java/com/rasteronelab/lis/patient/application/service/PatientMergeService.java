package com.rasteronelab.lis.patient.application.service;

import com.rasteronelab.lis.core.common.exception.BusinessRuleException;
import com.rasteronelab.lis.core.common.exception.NotFoundException;
import com.rasteronelab.lis.core.infrastructure.BranchContextHolder;
import com.rasteronelab.lis.patient.api.dto.PatientMergeAuditResponse;
import com.rasteronelab.lis.patient.api.mapper.PatientMergeAuditMapper;
import com.rasteronelab.lis.patient.domain.model.Patient;
import com.rasteronelab.lis.patient.domain.model.PatientMergeAudit;
import com.rasteronelab.lis.patient.domain.repository.PatientMergeAuditRepository;
import com.rasteronelab.lis.patient.domain.repository.PatientRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Service for merging duplicate patient records.
 * Keeps the primary record and soft-deletes the merged record,
 * preserving an audit trail via PatientMergeAudit.
 */
@Service
@Transactional
public class PatientMergeService {

    private final PatientRepository patientRepository;
    private final PatientMergeAuditRepository mergeAuditRepository;
    private final PatientMergeAuditMapper mergeAuditMapper;

    public PatientMergeService(PatientRepository patientRepository,
                               PatientMergeAuditRepository mergeAuditRepository,
                               PatientMergeAuditMapper mergeAuditMapper) {
        this.patientRepository = patientRepository;
        this.mergeAuditRepository = mergeAuditRepository;
        this.mergeAuditMapper = mergeAuditMapper;
    }

    /**
     * Merges a duplicate patient into a primary patient record.
     * The merged patient is soft-deleted after merging.
     *
     * @param primaryPatientId  the ID of the patient to keep
     * @param mergedPatientId   the ID of the duplicate patient to merge
     * @return the merge audit record
     */
    public PatientMergeAuditResponse mergePatients(UUID primaryPatientId, UUID mergedPatientId) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        if (primaryPatientId.equals(mergedPatientId)) {
            throw new BusinessRuleException("LIS-PAT-003", "Cannot merge a patient into themselves");
        }

        Patient primary = patientRepository.findByIdAndBranchIdAndIsDeletedFalse(primaryPatientId, branchId)
                .orElseThrow(() -> new NotFoundException("Patient", primaryPatientId));

        Patient merged = patientRepository.findByIdAndBranchIdAndIsDeletedFalse(mergedPatientId, branchId)
                .orElseThrow(() -> new NotFoundException("Patient", mergedPatientId));

        String currentUser = SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : "system";

        String mergeDetails = buildMergeDetails(primary, merged);

        // Fill in missing primary fields from merged patient
        if (primary.getPhone() == null && merged.getPhone() != null) {
            primary.setPhone(merged.getPhone());
        }
        if (primary.getEmail() == null && merged.getEmail() != null) {
            primary.setEmail(merged.getEmail());
        }
        if (primary.getAddressLine1() == null && merged.getAddressLine1() != null) {
            primary.setAddressLine1(merged.getAddressLine1());
            primary.setAddressLine2(merged.getAddressLine2());
            primary.setCity(merged.getCity());
            primary.setState(merged.getState());
            primary.setPostalCode(merged.getPostalCode());
            primary.setCountry(merged.getCountry());
        }
        if (primary.getEmergencyContactName() == null && merged.getEmergencyContactName() != null) {
            primary.setEmergencyContactName(merged.getEmergencyContactName());
            primary.setEmergencyContactPhone(merged.getEmergencyContactPhone());
        }
        if (primary.getBloodGroup() == null && merged.getBloodGroup() != null) {
            primary.setBloodGroup(merged.getBloodGroup());
        }

        patientRepository.save(primary);

        // Soft-delete merged patient
        merged.softDelete(currentUser);
        patientRepository.save(merged);

        // Create audit trail
        PatientMergeAudit audit = new PatientMergeAudit();
        audit.setBranchId(branchId);
        audit.setPrimaryPatientId(primaryPatientId);
        audit.setMergedPatientId(mergedPatientId);
        audit.setMergedBy(currentUser);
        audit.setMergedAt(LocalDateTime.now());
        audit.setMergeDetails(mergeDetails);

        return mergeAuditMapper.toResponse(mergeAuditRepository.save(audit));
    }

    /**
     * Gets the merge history for a patient.
     */
    @Transactional(readOnly = true)
    public List<PatientMergeAuditResponse> getMergeHistory(UUID primaryPatientId) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return mergeAuditRepository.findAllByPrimaryPatientIdAndBranchIdAndIsDeletedFalse(
                primaryPatientId, branchId)
                .stream()
                .map(mergeAuditMapper::toResponse)
                .toList();
    }

    private String buildMergeDetails(Patient primary, Patient merged) {
        return "{\"primary\":{\"uhid\":\"" + escapeJson(primary.getUhid()) + "\",\"name\":\"" +
                escapeJson(primary.getFirstName()) + " " + escapeJson(primary.getLastName()) + "\"}," +
                "\"merged\":{\"uhid\":\"" + escapeJson(merged.getUhid()) + "\",\"name\":\"" +
                escapeJson(merged.getFirstName()) + " " + escapeJson(merged.getLastName()) + "\"}}";
    }

    private String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
