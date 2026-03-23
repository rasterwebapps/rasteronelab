package com.rasteronelab.lis.patient.api.dto;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for PatientMergeAudit entity.
 * Includes merge operation details plus audit metadata.
 */
public class PatientMergeAuditResponse {

    private UUID id;
    private UUID branchId;
    private UUID primaryPatientId;
    private UUID mergedPatientId;
    private String mergedBy;
    private LocalDateTime mergedAt;
    private String mergeDetails;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    public PatientMergeAuditResponse() {
    }

    public PatientMergeAuditResponse(UUID id, UUID branchId, UUID primaryPatientId,
                                     UUID mergedPatientId, String mergedBy,
                                     LocalDateTime mergedAt, String mergeDetails,
                                     LocalDateTime createdAt, LocalDateTime updatedAt,
                                     String createdBy, String updatedBy) {
        this.id = id;
        this.branchId = branchId;
        this.primaryPatientId = primaryPatientId;
        this.mergedPatientId = mergedPatientId;
        this.mergedBy = mergedBy;
        this.mergedAt = mergedAt;
        this.mergeDetails = mergeDetails;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getBranchId() {
        return this.branchId;
    }

    public void setBranchId(UUID branchId) {
        this.branchId = branchId;
    }

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

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatientMergeAuditResponse that = (PatientMergeAuditResponse) o;
        return java.util.Objects.equals(this.id, that.id) &&
               java.util.Objects.equals(this.primaryPatientId, that.primaryPatientId) &&
               java.util.Objects.equals(this.mergedPatientId, that.mergedPatientId);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.id, this.primaryPatientId, this.mergedPatientId);
    }

    @Override
    public String toString() {
        return "PatientMergeAuditResponse{id=" + id +
               ", primaryPatientId=" + primaryPatientId +
               ", mergedPatientId=" + mergedPatientId +
               ", mergedBy=" + mergedBy +
               "}";
    }
}
