package com.rasteronelab.lis.admin.api.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Request DTO for creating or updating a Critical Value Config.
 */
public class CriticalValueConfigRequest {

    @NotNull(message = "Parameter ID is required")
    private UUID parameterId;

    private BigDecimal lowThreshold;

    private BigDecimal highThreshold;

    private Boolean notificationRequired;

    private Boolean autoFlag;

    private Boolean isActive;

    public CriticalValueConfigRequest() {
    }

    public CriticalValueConfigRequest(UUID parameterId, BigDecimal lowThreshold, BigDecimal highThreshold, Boolean notificationRequired, Boolean autoFlag, Boolean isActive) {
        this.parameterId = parameterId;
        this.lowThreshold = lowThreshold;
        this.highThreshold = highThreshold;
        this.notificationRequired = notificationRequired;
        this.autoFlag = autoFlag;
        this.isActive = isActive;
    }

    public UUID getParameterId() {
        return this.parameterId;
    }

    public BigDecimal getLowThreshold() {
        return this.lowThreshold;
    }

    public BigDecimal getHighThreshold() {
        return this.highThreshold;
    }

    public Boolean getNotificationRequired() {
        return this.notificationRequired;
    }

    public Boolean getAutoFlag() {
        return this.autoFlag;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public void setParameterId(UUID parameterId) {
        this.parameterId = parameterId;
    }

    public void setLowThreshold(BigDecimal lowThreshold) {
        this.lowThreshold = lowThreshold;
    }

    public void setHighThreshold(BigDecimal highThreshold) {
        this.highThreshold = highThreshold;
    }

    public void setNotificationRequired(Boolean notificationRequired) {
        this.notificationRequired = notificationRequired;
    }

    public void setAutoFlag(Boolean autoFlag) {
        this.autoFlag = autoFlag;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CriticalValueConfigRequest that = (CriticalValueConfigRequest) o;
        return java.util.Objects.equals(this.parameterId, that.parameterId) &&
               java.util.Objects.equals(this.lowThreshold, that.lowThreshold) &&
               java.util.Objects.equals(this.highThreshold, that.highThreshold) &&
               java.util.Objects.equals(this.notificationRequired, that.notificationRequired) &&
               java.util.Objects.equals(this.autoFlag, that.autoFlag) &&
               java.util.Objects.equals(this.isActive, that.isActive);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.parameterId, this.lowThreshold, this.highThreshold, this.notificationRequired, this.autoFlag, this.isActive);
    }

    @Override
    public String toString() {
        return "CriticalValueConfigRequest{parameterId=" + parameterId +
               ", lowThreshold=" + lowThreshold +
               ", highThreshold=" + highThreshold +
               ", notificationRequired=" + notificationRequired +
               ", autoFlag=" + autoFlag +
               ", isActive=" + isActive +
               "}";
    }

    public static CriticalValueConfigRequestBuilder builder() {
        return new CriticalValueConfigRequestBuilder();
    }

    public static class CriticalValueConfigRequestBuilder {
        private UUID parameterId;
        private BigDecimal lowThreshold;
        private BigDecimal highThreshold;
        private Boolean notificationRequired;
        private Boolean autoFlag;
        private Boolean isActive;

        CriticalValueConfigRequestBuilder() {
        }

        public CriticalValueConfigRequestBuilder parameterId(UUID parameterId) {
            this.parameterId = parameterId;
            return this;
        }

        public CriticalValueConfigRequestBuilder lowThreshold(BigDecimal lowThreshold) {
            this.lowThreshold = lowThreshold;
            return this;
        }

        public CriticalValueConfigRequestBuilder highThreshold(BigDecimal highThreshold) {
            this.highThreshold = highThreshold;
            return this;
        }

        public CriticalValueConfigRequestBuilder notificationRequired(Boolean notificationRequired) {
            this.notificationRequired = notificationRequired;
            return this;
        }

        public CriticalValueConfigRequestBuilder autoFlag(Boolean autoFlag) {
            this.autoFlag = autoFlag;
            return this;
        }

        public CriticalValueConfigRequestBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public CriticalValueConfigRequest build() {
            return new CriticalValueConfigRequest(this.parameterId, this.lowThreshold, this.highThreshold, this.notificationRequired, this.autoFlag, this.isActive);
        }
    }

}
