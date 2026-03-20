package com.rasteronelab.lis.admin.domain.model;

import com.rasteronelab.lis.core.domain.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * Microorganism entity for microbiology culture identification.
 * Extends BaseEntity for multi-branch support via branchId.
 */
@Entity
@Table(name = "microorganism")
public class Microorganism extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "gram_type")
    private String gramType;

    @Column(name = "organism_type")
    private String organismType;

    @Column(name = "clinical_significance")
    private String clinicalSignificance;

    @Column(name = "colony_morphology")
    private String colonyMorphology;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    public String getName() {
        return this.name;
    }

    public String getCode() {
        return this.code;
    }

    public String getGramType() {
        return this.gramType;
    }

    public String getOrganismType() {
        return this.organismType;
    }

    public String getClinicalSignificance() {
        return this.clinicalSignificance;
    }

    public String getColonyMorphology() {
        return this.colonyMorphology;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setGramType(String gramType) {
        this.gramType = gramType;
    }

    public void setOrganismType(String organismType) {
        this.organismType = organismType;
    }

    public void setClinicalSignificance(String clinicalSignificance) {
        this.clinicalSignificance = clinicalSignificance;
    }

    public void setColonyMorphology(String colonyMorphology) {
        this.colonyMorphology = colonyMorphology;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

}
