package com.rasteronelab.lis.patient.domain.model;

import com.rasteronelab.lis.core.domain.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * PatientVisit entity representing a single visit by a patient to a branch.
 * Extends BaseEntity for multi-branch support via branchId.
 */
@Entity
@Table(name = "patient_visit")
public class PatientVisit extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(name = "patient_id", insertable = false, updatable = false)
    private UUID patientId;

    @Column(name = "visit_number", nullable = false)
    private String visitNumber;

    @Column(name = "visit_date", nullable = false)
    private LocalDateTime visitDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "visit_type")
    private VisitType visitType;

    @Column(name = "referring_doctor_id")
    private UUID referringDoctorId;

    @Column(name = "clinical_notes")
    private String clinicalNotes;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    public Patient getPatient() {
        return this.patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public UUID getPatientId() {
        return this.patientId;
    }

    public void setPatientId(UUID patientId) {
        this.patientId = patientId;
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
}
