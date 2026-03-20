package com.rasteronelab.lis.admin.domain.model;

import com.rasteronelab.lis.core.domain.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Reference Range entity (L3 branch-level).
 * Extends BaseEntity for multi-branch support via branchId.
 * Defines normal and critical ranges for a parameter by gender and age group.
 */
@Entity
@Table(name = "reference_range")
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

    public UUID getParameterId() {
        return this.parameterId;
    }

    public Parameter getParameter() {
        return this.parameter;
    }

    public String getGender() {
        return this.gender;
    }

    public BigDecimal getAgeMin() {
        return this.ageMin;
    }

    public BigDecimal getAgeMax() {
        return this.ageMax;
    }

    public String getAgeUnit() {
        return this.ageUnit;
    }

    public BigDecimal getNormalMin() {
        return this.normalMin;
    }

    public BigDecimal getNormalMax() {
        return this.normalMax;
    }

    public BigDecimal getCriticalLow() {
        return this.criticalLow;
    }

    public BigDecimal getCriticalHigh() {
        return this.criticalHigh;
    }

    public String getNormalText() {
        return this.normalText;
    }

    public String getUnit() {
        return this.unit;
    }

    public Boolean getIsPregnancy() {
        return this.isPregnancy;
    }

    public String getDisplayText() {
        return this.displayText;
    }

    public void setParameterId(UUID parameterId) {
        this.parameterId = parameterId;
    }

    public void setParameter(Parameter parameter) {
        this.parameter = parameter;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAgeMin(BigDecimal ageMin) {
        this.ageMin = ageMin;
    }

    public void setAgeMax(BigDecimal ageMax) {
        this.ageMax = ageMax;
    }

    public void setAgeUnit(String ageUnit) {
        this.ageUnit = ageUnit;
    }

    public void setNormalMin(BigDecimal normalMin) {
        this.normalMin = normalMin;
    }

    public void setNormalMax(BigDecimal normalMax) {
        this.normalMax = normalMax;
    }

    public void setCriticalLow(BigDecimal criticalLow) {
        this.criticalLow = criticalLow;
    }

    public void setCriticalHigh(BigDecimal criticalHigh) {
        this.criticalHigh = criticalHigh;
    }

    public void setNormalText(String normalText) {
        this.normalText = normalText;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setIsPregnancy(Boolean isPregnancy) {
        this.isPregnancy = isPregnancy;
    }

    public void setDisplayText(String displayText) {
        this.displayText = displayText;
    }

}
