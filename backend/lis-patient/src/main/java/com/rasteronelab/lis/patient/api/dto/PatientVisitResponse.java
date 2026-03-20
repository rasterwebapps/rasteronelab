package com.rasteronelab.lis.patient.api.dto;

import com.rasteronelab.lis.patient.domain.model.VisitType;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for PatientVisit entity.
 * Includes all visit fields plus audit metadata.
 */
public class PatientVisitResponse {

    private UUID id;
    private UUID patientId;
    private UUID branchId;
    private String visitNumber;
    private LocalDateTime visitDate;
    private VisitType visitType;
    private UUID referringDoctorId;
    private String clinicalNotes;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    public PatientVisitResponse() {
    }

    public PatientVisitResponse(UUID id, UUID patientId, UUID branchId, String visitNumber,
                                LocalDateTime visitDate, VisitType visitType,
                                UUID referringDoctorId, String clinicalNotes, Boolean isActive,
                                LocalDateTime createdAt, LocalDateTime updatedAt,
                                String createdBy, String updatedBy) {
        this.id = id;
        this.patientId = patientId;
        this.branchId = branchId;
        this.visitNumber = visitNumber;
        this.visitDate = visitDate;
        this.visitType = visitType;
        this.referringDoctorId = referringDoctorId;
        this.clinicalNotes = clinicalNotes;
        this.isActive = isActive;
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

    public UUID getPatientId() {
        return this.patientId;
    }

    public void setPatientId(UUID patientId) {
        this.patientId = patientId;
    }

    public UUID getBranchId() {
        return this.branchId;
    }

    public void setBranchId(UUID branchId) {
        this.branchId = branchId;
    }

    public String getVisitNumber() {
        return this.visitNumber;
    }

    public void setVisitNumber(String visitNumber) {
        this.visitNumber = visitNumber;
    }

    public LocalDateTime getVisitDate() {
        return this.visitDate;
    }

    public void setVisitDate(LocalDateTime visitDate) {
        this.visitDate = visitDate;
    }

    public VisitType getVisitType() {
        return this.visitType;
    }

    public void setVisitType(VisitType visitType) {
        this.visitType = visitType;
    }

    public UUID getReferringDoctorId() {
        return this.referringDoctorId;
    }

    public void setReferringDoctorId(UUID referringDoctorId) {
        this.referringDoctorId = referringDoctorId;
    }

    public String getClinicalNotes() {
        return this.clinicalNotes;
    }

    public void setClinicalNotes(String clinicalNotes) {
        this.clinicalNotes = clinicalNotes;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
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
        PatientVisitResponse that = (PatientVisitResponse) o;
        return java.util.Objects.equals(this.id, that.id) &&
               java.util.Objects.equals(this.patientId, that.patientId) &&
               java.util.Objects.equals(this.visitNumber, that.visitNumber);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.id, this.patientId, this.visitNumber);
    }

    @Override
    public String toString() {
        return "PatientVisitResponse{id=" + id +
               ", patientId=" + patientId +
               ", visitNumber=" + visitNumber +
               ", visitType=" + visitType +
               "}";
    }

    public static PatientVisitResponseBuilder builder() {
        return new PatientVisitResponseBuilder();
    }

    public static class PatientVisitResponseBuilder {
        private UUID id;
        private UUID patientId;
        private UUID branchId;
        private String visitNumber;
        private LocalDateTime visitDate;
        private VisitType visitType;
        private UUID referringDoctorId;
        private String clinicalNotes;
        private Boolean isActive;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String createdBy;
        private String updatedBy;

        PatientVisitResponseBuilder() {
        }

        public PatientVisitResponseBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public PatientVisitResponseBuilder patientId(UUID patientId) {
            this.patientId = patientId;
            return this;
        }

        public PatientVisitResponseBuilder branchId(UUID branchId) {
            this.branchId = branchId;
            return this;
        }

        public PatientVisitResponseBuilder visitNumber(String visitNumber) {
            this.visitNumber = visitNumber;
            return this;
        }

        public PatientVisitResponseBuilder visitDate(LocalDateTime visitDate) {
            this.visitDate = visitDate;
            return this;
        }

        public PatientVisitResponseBuilder visitType(VisitType visitType) {
            this.visitType = visitType;
            return this;
        }

        public PatientVisitResponseBuilder referringDoctorId(UUID referringDoctorId) {
            this.referringDoctorId = referringDoctorId;
            return this;
        }

        public PatientVisitResponseBuilder clinicalNotes(String clinicalNotes) {
            this.clinicalNotes = clinicalNotes;
            return this;
        }

        public PatientVisitResponseBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public PatientVisitResponseBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public PatientVisitResponseBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public PatientVisitResponseBuilder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public PatientVisitResponseBuilder updatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public PatientVisitResponse build() {
            return new PatientVisitResponse(this.id, this.patientId, this.branchId,
                    this.visitNumber, this.visitDate, this.visitType, this.referringDoctorId,
                    this.clinicalNotes, this.isActive, this.createdAt, this.updatedAt,
                    this.createdBy, this.updatedBy);
        }
    }

}
