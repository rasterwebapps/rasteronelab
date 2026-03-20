package com.rasteronelab.lis.admin.domain.model;

import com.rasteronelab.lis.core.domain.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * InsuranceTariff entity (L3 branch-level).
 * Extends BaseEntity for multi-branch support via branchId.
 * Represents an insurance tariff rate for a specific insurance plan and test within a branch.
 */
@Entity
@Table(name = "insurance_tariff")
public class InsuranceTariff extends BaseEntity {

    @Column(name = "insurance_name", nullable = false)
    private String insuranceName;

    @Column(name = "plan_name", nullable = false)
    private String planName;

    @Column(name = "test_id")
    private UUID testId;

    @Column(name = "tariff_rate", nullable = false)
    private BigDecimal tariffRate;

    @Column(name = "effective_from")
    private LocalDate effectiveFrom;

    @Column(name = "effective_to")
    private LocalDate effectiveTo;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    public String getInsuranceName() {
        return this.insuranceName;
    }

    public String getPlanName() {
        return this.planName;
    }

    public UUID getTestId() {
        return this.testId;
    }

    public BigDecimal getTariffRate() {
        return this.tariffRate;
    }

    public LocalDate getEffectiveFrom() {
        return this.effectiveFrom;
    }

    public LocalDate getEffectiveTo() {
        return this.effectiveTo;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public void setInsuranceName(String insuranceName) {
        this.insuranceName = insuranceName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public void setTestId(UUID testId) {
        this.testId = testId;
    }

    public void setTariffRate(BigDecimal tariffRate) {
        this.tariffRate = tariffRate;
    }

    public void setEffectiveFrom(LocalDate effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    public void setEffectiveTo(LocalDate effectiveTo) {
        this.effectiveTo = effectiveTo;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

}
