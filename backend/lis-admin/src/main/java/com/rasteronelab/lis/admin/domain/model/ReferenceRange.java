package com.rasteronelab.lis.admin.domain.model;

import com.rasteronelab.lis.core.domain.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Reference Range entity (L3 branch-level).
 * Extends BaseEntity for multi-branch support via branchId.
 * Defines normal and critical ranges for a parameter by gender and age group.
 */
@Entity
@Table(name = "reference_range")
@Getter
@Setter
public class ReferenceRange extends BaseEntity {

    @Column(name = "parameter_id", nullable = false, insertable = false, updatable = false)
    private UUID parameterId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parameter_id", nullable = false)
    private Parameter parameter;

    @Column(name = "gender")
    private String gender;

    @Column(name = "age_min")
    private BigDecimal ageMin;

    @Column(name = "age_max")
    private BigDecimal ageMax;

    @Column(name = "age_unit")
    private String ageUnit = "YEARS";

    @Column(name = "normal_min")
    private BigDecimal normalMin;

    @Column(name = "normal_max")
    private BigDecimal normalMax;

    @Column(name = "critical_low")
    private BigDecimal criticalLow;

    @Column(name = "critical_high")
    private BigDecimal criticalHigh;

    @Column(name = "normal_text")
    private String normalText;

    @Column(name = "unit")
    private String unit;

    @Column(name = "is_pregnancy", nullable = false)
    private Boolean isPregnancy = false;

    @Column(name = "display_text")
    private String displayText;
}
