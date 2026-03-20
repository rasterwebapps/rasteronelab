package com.rasteronelab.lis.admin.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for CriticalValueConfig entity.
 * Includes parameterName for display purposes.
 */
public class CriticalValueConfigResponse {

    private UUID id;
    private UUID branchId;
    private UUID parameterId;
    private String parameterName;
    private BigDecimal lowThreshold;
    private BigDecimal highThreshold;
    private Boolean notificationRequired;
    private Boolean autoFlag;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    public CriticalValueConfigResponse() {
    }

    public CriticalValueConfigResponse(UUID id, UUID branchId, UUID parameterId, String parameterName, BigDecimal lowThreshold, BigDecimal highThreshold, Boolean notificationRequired, Boolean autoFlag, Boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String updatedBy) {
        this.id = id;
        this.branchId = branchId;
        this.parameterId = parameterId;
        this.parameterName = parameterName;
        this.lowThreshold = lowThreshold;
        this.highThreshold = highThreshold;
        this.notificationRequired = notificationRequired;
        this.autoFlag = autoFlag;
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

    public UUID getParameterId() {
        return this.parameterId;
    }

    public String getParameterName() {
        return this.parameterName;
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

    public void setParameterId(UUID parameterId) {
        this.parameterId = parameterId;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
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
        CriticalValueConfigResponse that = (CriticalValueConfigResponse) o;
        return java.util.Objects.equals(this.id, that.id) &&
               java.util.Objects.equals(this.branchId, that.branchId) &&
               java.util.Objects.equals(this.parameterId, that.parameterId) &&
               java.util.Objects.equals(this.parameterName, that.parameterName) &&
               java.util.Objects.equals(this.lowThreshold, that.lowThreshold) &&
               java.util.Objects.equals(this.highThreshold, that.highThreshold) &&
               java.util.Objects.equals(this.notificationRequired, that.notificationRequired) &&
               java.util.Objects.equals(this.autoFlag, that.autoFlag) &&
               java.util.Objects.equals(this.isActive, that.isActive) &&
               java.util.Objects.equals(this.createdAt, that.createdAt) &&
               java.util.Objects.equals(this.updatedAt, that.updatedAt) &&
               java.util.Objects.equals(this.createdBy, that.createdBy) &&
               java.util.Objects.equals(this.updatedBy, that.updatedBy);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.id, this.branchId, this.parameterId, this.parameterName, this.lowThreshold, this.highThreshold, this.notificationRequired, this.autoFlag, this.isActive, this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
    }

    @Override
    public String toString() {
        return "CriticalValueConfigResponse{id=" + id +
               ", branchId=" + branchId +
               ", parameterId=" + parameterId +
               ", parameterName=" + parameterName +
               ", lowThreshold=" + lowThreshold +
               ", highThreshold=" + highThreshold +
               ", notificationRequired=" + notificationRequired +
               ", autoFlag=" + autoFlag +
               ", isActive=" + isActive +
               ", createdAt=" + createdAt +
               ", updatedAt=" + updatedAt +
               ", createdBy=" + createdBy +
               ", updatedBy=" + updatedBy +
               "}";
    }

    public static CriticalValueConfigResponseBuilder builder() {
        return new CriticalValueConfigResponseBuilder();
    }

    public static class CriticalValueConfigResponseBuilder {
        private UUID id;
        private UUID branchId;
        private UUID parameterId;
        private String parameterName;
        private BigDecimal lowThreshold;
        private BigDecimal highThreshold;
        private Boolean notificationRequired;
        private Boolean autoFlag;
        private Boolean isActive;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String createdBy;
        private String updatedBy;

        CriticalValueConfigResponseBuilder() {
        }

        public CriticalValueConfigResponseBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public CriticalValueConfigResponseBuilder branchId(UUID branchId) {
            this.branchId = branchId;
            return this;
        }

        public CriticalValueConfigResponseBuilder parameterId(UUID parameterId) {
            this.parameterId = parameterId;
            return this;
        }

        public CriticalValueConfigResponseBuilder parameterName(String parameterName) {
            this.parameterName = parameterName;
            return this;
        }

        public CriticalValueConfigResponseBuilder lowThreshold(BigDecimal lowThreshold) {
            this.lowThreshold = lowThreshold;
            return this;
        }

        public CriticalValueConfigResponseBuilder highThreshold(BigDecimal highThreshold) {
            this.highThreshold = highThreshold;
            return this;
        }

        public CriticalValueConfigResponseBuilder notificationRequired(Boolean notificationRequired) {
            this.notificationRequired = notificationRequired;
            return this;
        }

        public CriticalValueConfigResponseBuilder autoFlag(Boolean autoFlag) {
            this.autoFlag = autoFlag;
            return this;
        }

        public CriticalValueConfigResponseBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public CriticalValueConfigResponseBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public CriticalValueConfigResponseBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public CriticalValueConfigResponseBuilder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public CriticalValueConfigResponseBuilder updatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public CriticalValueConfigResponse build() {
            return new CriticalValueConfigResponse(this.id, this.branchId, this.parameterId, this.parameterName, this.lowThreshold, this.highThreshold, this.notificationRequired, this.autoFlag, this.isActive, this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
        }
    }

}
