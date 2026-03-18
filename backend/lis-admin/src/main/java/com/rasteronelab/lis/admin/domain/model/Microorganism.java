package com.rasteronelab.lis.admin.domain.model;

import com.rasteronelab.lis.core.domain.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Microorganism entity for microbiology culture identification.
 * Extends BaseEntity for multi-branch support via branchId.
 */
@Entity
@Table(name = "microorganism")
@Getter
@Setter
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
}
