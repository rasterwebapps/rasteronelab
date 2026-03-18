package com.rasteronelab.lis.admin.api.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Request DTO for creating or updating a Delta Check Config.
 */
public class DeltaCheckConfigRequest {

    @NotNull(message = "Parameter ID is required")
    private UUID parameterId;

    @NotNull(message = "Percentage threshold is required")
    private BigDecimal percentageThreshold;

    private BigDecimal absoluteThreshold;

    private Integer timeWindowHours;

    private Boolean isActive;

    public DeltaCheckConfigRequest() {
    }

    public DeltaCheckConfigRequest(UUID parameterId, BigDecimal percentageThreshold, BigDecimal absoluteThreshold, Integer timeWindowHours, Boolean isActive) {
        this.parameterId = parameterId;
        this.percentageThreshold = percentageThreshold;
        this.absoluteThreshold = absoluteThreshold;
        this.timeWindowHours = timeWindowHours;
        this.isActive = isActive;
    }

    public UUID getParameterId() {
        return this.parameterId;
    }

    public BigDecimal getPercentageThreshold() {
        return this.percentageThreshold;
    }

    public BigDecimal getAbsoluteThreshold() {
        return this.absoluteThreshold;
    }

    public Integer getTimeWindowHours() {
        return this.timeWindowHours;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public void setParameterId(UUID parameterId) {
        this.parameterId = parameterId;
    }

    public void setPercentageThreshold(BigDecimal percentageThreshold) {
        this.percentageThreshold = percentageThreshold;
    }

    public void setAbsoluteThreshold(BigDecimal absoluteThreshold) {
        this.absoluteThreshold = absoluteThreshold;
    }

    public void setTimeWindowHours(Integer timeWindowHours) {
        this.timeWindowHours = timeWindowHours;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeltaCheckConfigRequest that = (DeltaCheckConfigRequest) o;
        return java.util.Objects.equals(this.parameterId, that.parameterId) &&
               java.util.Objects.equals(this.percentageThreshold, that.percentageThreshold) &&
               java.util.Objects.equals(this.absoluteThreshold, that.absoluteThreshold) &&
               java.util.Objects.equals(this.timeWindowHours, that.timeWindowHours) &&
               java.util.Objects.equals(this.isActive, that.isActive);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.parameterId, this.percentageThreshold, this.absoluteThreshold, this.timeWindowHours, this.isActive);
    }

    @Override
    public String toString() {
        return "DeltaCheckConfigRequest{parameterId=" + parameterId +
               ", percentageThreshold=" + percentageThreshold +
               ", absoluteThreshold=" + absoluteThreshold +
               ", timeWindowHours=" + timeWindowHours +
               ", isActive=" + isActive +
               "}";
    }

    public static DeltaCheckConfigRequestBuilder builder() {
        return new DeltaCheckConfigRequestBuilder();
    }

    public static class DeltaCheckConfigRequestBuilder {
        private UUID parameterId;
        private BigDecimal percentageThreshold;
        private BigDecimal absoluteThreshold;
        private Integer timeWindowHours;
        private Boolean isActive;

        DeltaCheckConfigRequestBuilder() {
        }

        public DeltaCheckConfigRequestBuilder parameterId(UUID parameterId) {
            this.parameterId = parameterId;
            return this;
        }

        public DeltaCheckConfigRequestBuilder percentageThreshold(BigDecimal percentageThreshold) {
            this.percentageThreshold = percentageThreshold;
            return this;
        }

        public DeltaCheckConfigRequestBuilder absoluteThreshold(BigDecimal absoluteThreshold) {
            this.absoluteThreshold = absoluteThreshold;
            return this;
        }

        public DeltaCheckConfigRequestBuilder timeWindowHours(Integer timeWindowHours) {
            this.timeWindowHours = timeWindowHours;
            return this;
        }

        public DeltaCheckConfigRequestBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public DeltaCheckConfigRequest build() {
            return new DeltaCheckConfigRequest(this.parameterId, this.percentageThreshold, this.absoluteThreshold, this.timeWindowHours, this.isActive);
        }
    }

}
