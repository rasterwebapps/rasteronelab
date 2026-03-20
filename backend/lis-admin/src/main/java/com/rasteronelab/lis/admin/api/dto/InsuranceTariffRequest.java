package com.rasteronelab.lis.admin.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Request DTO for creating or updating an InsuranceTariff.
 */
public class InsuranceTariffRequest {

    @NotBlank(message = "Insurance name is required")
    @Size(max = 200, message = "Insurance name must not exceed 200 characters")
    private String insuranceName;

    @NotBlank(message = "Plan name is required")
    @Size(max = 200, message = "Plan name must not exceed 200 characters")
    private String planName;

    private UUID testId;

    @NotNull(message = "Tariff rate is required")
    @DecimalMin(value = "0", message = "Tariff rate must be zero or positive")
    private BigDecimal tariffRate;

    private LocalDate effectiveFrom;

    private LocalDate effectiveTo;

    private Boolean isActive;

    public InsuranceTariffRequest() {
    }

    public InsuranceTariffRequest(String insuranceName, String planName, UUID testId, BigDecimal tariffRate, LocalDate effectiveFrom, LocalDate effectiveTo, Boolean isActive) {
        this.insuranceName = insuranceName;
        this.planName = planName;
        this.testId = testId;
        this.tariffRate = tariffRate;
        this.effectiveFrom = effectiveFrom;
        this.effectiveTo = effectiveTo;
        this.isActive = isActive;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InsuranceTariffRequest that = (InsuranceTariffRequest) o;
        return java.util.Objects.equals(this.insuranceName, that.insuranceName) &&
               java.util.Objects.equals(this.planName, that.planName) &&
               java.util.Objects.equals(this.testId, that.testId) &&
               java.util.Objects.equals(this.tariffRate, that.tariffRate) &&
               java.util.Objects.equals(this.effectiveFrom, that.effectiveFrom) &&
               java.util.Objects.equals(this.effectiveTo, that.effectiveTo) &&
               java.util.Objects.equals(this.isActive, that.isActive);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.insuranceName, this.planName, this.testId, this.tariffRate, this.effectiveFrom, this.effectiveTo, this.isActive);
    }

    @Override
    public String toString() {
        return "InsuranceTariffRequest{insuranceName=" + insuranceName +
               ", planName=" + planName +
               ", testId=" + testId +
               ", tariffRate=" + tariffRate +
               ", effectiveFrom=" + effectiveFrom +
               ", effectiveTo=" + effectiveTo +
               ", isActive=" + isActive +
               "}";
    }

    public static InsuranceTariffRequestBuilder builder() {
        return new InsuranceTariffRequestBuilder();
    }

    public static class InsuranceTariffRequestBuilder {
        private String insuranceName;
        private String planName;
        private UUID testId;
        private BigDecimal tariffRate;
        private LocalDate effectiveFrom;
        private LocalDate effectiveTo;
        private Boolean isActive;

        InsuranceTariffRequestBuilder() {
        }

        public InsuranceTariffRequestBuilder insuranceName(String insuranceName) {
            this.insuranceName = insuranceName;
            return this;
        }

        public InsuranceTariffRequestBuilder planName(String planName) {
            this.planName = planName;
            return this;
        }

        public InsuranceTariffRequestBuilder testId(UUID testId) {
            this.testId = testId;
            return this;
        }

        public InsuranceTariffRequestBuilder tariffRate(BigDecimal tariffRate) {
            this.tariffRate = tariffRate;
            return this;
        }

        public InsuranceTariffRequestBuilder effectiveFrom(LocalDate effectiveFrom) {
            this.effectiveFrom = effectiveFrom;
            return this;
        }

        public InsuranceTariffRequestBuilder effectiveTo(LocalDate effectiveTo) {
            this.effectiveTo = effectiveTo;
            return this;
        }

        public InsuranceTariffRequestBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public InsuranceTariffRequest build() {
            return new InsuranceTariffRequest(this.insuranceName, this.planName, this.testId, this.tariffRate, this.effectiveFrom, this.effectiveTo, this.isActive);
        }
    }

}
