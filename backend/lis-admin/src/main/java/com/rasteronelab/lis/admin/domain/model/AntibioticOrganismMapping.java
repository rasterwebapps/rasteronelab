package com.rasteronelab.lis.admin.domain.model;

import com.rasteronelab.lis.core.domain.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.util.UUID;

/**
 * Mapping between Antibiotic and Microorganism for sensitivity panels.
 * Extends BaseEntity for multi-branch support via branchId.
 */
@Entity
@Table(name = "antibiotic_organism_mapping", uniqueConstraints = {
        @UniqueConstraint(name = "uq_antibiotic_organism",
                columnNames = {"branch_id", "antibiotic_id", "microorganism_id"})
})
public class AntibioticOrganismMapping extends BaseEntity {

    @Column(name = "antibiotic_id", nullable = false, insertable = false, updatable = false)
    private UUID antibioticId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "antibiotic_id", nullable = false)
    private Antibiotic antibiotic;

    @Column(name = "microorganism_id", nullable = false, insertable = false, updatable = false)
    private UUID microorganismId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "microorganism_id", nullable = false)
    private Microorganism microorganism;

    @Column(name = "susceptibility", nullable = false)
    private String susceptibility = "UNKNOWN";

    @Column(name = "is_default_panel", nullable = false)
    private Boolean isDefaultPanel = false;

    public UUID getAntibioticId() {
        return this.antibioticId;
    }

    public Antibiotic getAntibiotic() {
        return this.antibiotic;
    }

    public UUID getMicroorganismId() {
        return this.microorganismId;
    }

    public Microorganism getMicroorganism() {
        return this.microorganism;
    }

    public String getSusceptibility() {
        return this.susceptibility;
    }

    public Boolean getIsDefaultPanel() {
        return this.isDefaultPanel;
    }

    public void setAntibioticId(UUID antibioticId) {
        this.antibioticId = antibioticId;
    }

    public void setAntibiotic(Antibiotic antibiotic) {
        this.antibiotic = antibiotic;
    }

    public void setMicroorganismId(UUID microorganismId) {
        this.microorganismId = microorganismId;
    }

    public void setMicroorganism(Microorganism microorganism) {
        this.microorganism = microorganism;
    }

    public void setSusceptibility(String susceptibility) {
        this.susceptibility = susceptibility;
    }

    public void setIsDefaultPanel(Boolean isDefaultPanel) {
        this.isDefaultPanel = isDefaultPanel;
    }

}
