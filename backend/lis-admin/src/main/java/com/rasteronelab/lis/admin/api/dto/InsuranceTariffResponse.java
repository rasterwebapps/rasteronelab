package com.rasteronelab.lis.admin.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for InsuranceTariff entity.
 * Includes all entity fields plus audit metadata.
 */
public class InsuranceTariffResponse {

    private UUID id;
    private UUID branchId;
    private String insuranceName;
    private String planName;
    private UUID testId;
    private BigDecimal tariffRate;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    public InsuranceTariffResponse() {
    }

    public InsuranceTariffResponse(UUID id, UUID branchId, String insuranceName, String planName, UUID testId, BigDecimal tariffRate, LocalDate effectiveFrom, LocalDate effectiveTo, Boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String updatedBy) {
        this.id = id;
        this.branchId = branchId;
        this.insuranceName = insuranceName;
        this.planName = planName;
        this.testId = testId;
        this.tariffRate = tariffRate;
        this.effectiveFrom = effectiveFrom;
        this.effectiveTo = effectiveTo;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }

    public UUID getId() {
        return this.id;
    }

    public UUID getBranchId() {
        return this.branchId;
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

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setBranchId(UUID branchId) {
        this.branchId = branchId;
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

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InsuranceTariffResponse that = (InsuranceTariffResponse) o;
        return java.util.Objects.equals(this.id, that.id) &&
               java.util.Objects.equals(this.branchId, that.branchId) &&
               java.util.Objects.equals(this.insuranceName, that.insuranceName) &&
               java.util.Objects.equals(this.planName, that.planName) &&
               java.util.Objects.equals(this.testId, that.testId) &&
               java.util.Objects.equals(this.tariffRate, that.tariffRate) &&
               java.util.Objects.equals(this.effectiveFrom, that.effectiveFrom) &&
               java.util.Objects.equals(this.effectiveTo, that.effectiveTo) &&
               java.util.Objects.equals(this.isActive, that.isActive) &&
               java.util.Objects.equals(this.createdAt, that.createdAt) &&
               java.util.Objects.equals(this.updatedAt, that.updatedAt) &&
               java.util.Objects.equals(this.createdBy, that.createdBy) &&
               java.util.Objects.equals(this.updatedBy, that.updatedBy);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.id, this.branchId, this.insuranceName, this.planName, this.testId, this.tariffRate, this.effectiveFrom, this.effectiveTo, this.isActive, this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
    }

    @Override
    public String toString() {
        return "InsuranceTariffResponse{id=" + id +
               ", branchId=" + branchId +
               ", insuranceName=" + insuranceName +
               ", planName=" + planName +
               ", testId=" + testId +
               ", tariffRate=" + tariffRate +
               ", effectiveFrom=" + effectiveFrom +
               ", effectiveTo=" + effectiveTo +
               ", isActive=" + isActive +
               ", createdAt=" + createdAt +
               ", updatedAt=" + updatedAt +
               ", createdBy=" + createdBy +
               ", updatedBy=" + updatedBy +
               "}";
    }

    public static InsuranceTariffResponseBuilder builder() {
        return new InsuranceTariffResponseBuilder();
    }

    public static class InsuranceTariffResponseBuilder {
        private UUID id;
        private UUID branchId;
        private String insuranceName;
        private String planName;
        private UUID testId;
        private BigDecimal tariffRate;
        private LocalDate effectiveFrom;
        private LocalDate effectiveTo;
        private Boolean isActive;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String createdBy;
        private String updatedBy;

        InsuranceTariffResponseBuilder() {
        }

        public InsuranceTariffResponseBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public InsuranceTariffResponseBuilder branchId(UUID branchId) {
            this.branchId = branchId;
            return this;
        }

        public InsuranceTariffResponseBuilder insuranceName(String insuranceName) {
            this.insuranceName = insuranceName;
            return this;
        }

        public InsuranceTariffResponseBuilder planName(String planName) {
            this.planName = planName;
            return this;
        }

        public InsuranceTariffResponseBuilder testId(UUID testId) {
            this.testId = testId;
            return this;
        }

        public InsuranceTariffResponseBuilder tariffRate(BigDecimal tariffRate) {
            this.tariffRate = tariffRate;
            return this;
        }

        public InsuranceTariffResponseBuilder effectiveFrom(LocalDate effectiveFrom) {
            this.effectiveFrom = effectiveFrom;
            return this;
        }

        public InsuranceTariffResponseBuilder effectiveTo(LocalDate effectiveTo) {
            this.effectiveTo = effectiveTo;
            return this;
        }

        public InsuranceTariffResponseBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public InsuranceTariffResponseBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public InsuranceTariffResponseBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public InsuranceTariffResponseBuilder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public InsuranceTariffResponseBuilder updatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public InsuranceTariffResponse build() {
            return new InsuranceTariffResponse(this.id, this.branchId, this.insuranceName, this.planName, this.testId, this.tariffRate, this.effectiveFrom, this.effectiveTo, this.isActive, this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
        }
    }

}
