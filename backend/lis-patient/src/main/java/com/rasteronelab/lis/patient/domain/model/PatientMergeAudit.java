package com.rasteronelab.lis.patient.domain.model;

import com.rasteronelab.lis.core.domain.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * PatientMergeAudit entity tracking patient merge operations.
 * Extends BaseEntity for multi-branch support via branchId.
 * Stores merge details as JSONB for flexible audit trail.
 */
@Entity
@Table(name = "patient_merge_audit")
public class PatientMergeAudit extends BaseEntity {

    @Column(name = "primary_patient_id", nullable = false)
    private UUID primaryPatientId;

    @Column(name = "merged_patient_id", nullable = false)
    private UUID mergedPatientId;

    @Column(name = "merged_by", nullable = false)
    private String mergedBy;

    @Column(name = "merged_at", nullable = false)
    private LocalDateTime mergedAt;

    @Column(name = "merge_details", columnDefinition = "jsonb")
    private String mergeDetails;

    public UUID getPrimaryPatientId() {
        return this.primaryPatientId;
    }

    public void setPrimaryPatientId(UUID primaryPatientId) {
        this.primaryPatientId = primaryPatientId;
    }

    public UUID getMergedPatientId() {
        return this.mergedPatientId;
    }

    public void setMergedPatientId(UUID mergedPatientId) {
        this.mergedPatientId = mergedPatientId;
    }

    public String getMergedBy() {
        return this.mergedBy;
    }

    public void setMergedBy(String mergedBy) {
        this.mergedBy = mergedBy;
    }

    public LocalDateTime getMergedAt() {
        return this.mergedAt;
    }

    public void setMergedAt(LocalDateTime mergedAt) {
        this.mergedAt = mergedAt;
    }

    public String getMergeDetails() {
        return this.mergeDetails;
    }

    public void setMergeDetails(String mergeDetails) {
        this.mergeDetails = mergeDetails;
    }
}
