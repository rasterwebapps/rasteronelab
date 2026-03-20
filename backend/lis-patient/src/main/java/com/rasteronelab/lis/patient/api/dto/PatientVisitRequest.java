package com.rasteronelab.lis.patient.api.dto;

import com.rasteronelab.lis.patient.domain.model.VisitType;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Request DTO for creating or updating a PatientVisit.
 */
public class PatientVisitRequest {

    @NotNull(message = "Visit type is required")
    private VisitType visitType;

    private UUID referringDoctorId;

    private String clinicalNotes;

    public PatientVisitRequest() {
    }

    public PatientVisitRequest(VisitType visitType, UUID referringDoctorId, String clinicalNotes) {
        this.visitType = visitType;
        this.referringDoctorId = referringDoctorId;
        this.clinicalNotes = clinicalNotes;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatientVisitRequest that = (PatientVisitRequest) o;
        return java.util.Objects.equals(this.visitType, that.visitType) &&
               java.util.Objects.equals(this.referringDoctorId, that.referringDoctorId) &&
               java.util.Objects.equals(this.clinicalNotes, that.clinicalNotes);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.visitType, this.referringDoctorId, this.clinicalNotes);
    }

    @Override
    public String toString() {
        return "PatientVisitRequest{visitType=" + visitType +
               ", referringDoctorId=" + referringDoctorId +
               "}";
    }

    public static PatientVisitRequestBuilder builder() {
        return new PatientVisitRequestBuilder();
    }

    public static class PatientVisitRequestBuilder {
        private VisitType visitType;
        private UUID referringDoctorId;
        private String clinicalNotes;

        PatientVisitRequestBuilder() {
        }

        public PatientVisitRequestBuilder visitType(VisitType visitType) {
            this.visitType = visitType;
            return this;
        }

        public PatientVisitRequestBuilder referringDoctorId(UUID referringDoctorId) {
            this.referringDoctorId = referringDoctorId;
            return this;
        }

        public PatientVisitRequestBuilder clinicalNotes(String clinicalNotes) {
            this.clinicalNotes = clinicalNotes;
            return this;
        }

        public PatientVisitRequest build() {
            return new PatientVisitRequest(this.visitType, this.referringDoctorId, this.clinicalNotes);
        }
    }

}
