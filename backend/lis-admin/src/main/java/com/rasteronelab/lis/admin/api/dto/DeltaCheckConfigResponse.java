package com.rasteronelab.lis.admin.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for DeltaCheckConfig entity.
 * Includes parameterName for display purposes.
 */
public class DeltaCheckConfigResponse {

    private UUID id;
    private UUID branchId;
    private UUID parameterId;
    private String parameterName;
    private BigDecimal percentageThreshold;
    private BigDecimal absoluteThreshold;
    private Integer timeWindowHours;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    public DeltaCheckConfigResponse() {
    }

    public DeltaCheckConfigResponse(UUID id, UUID branchId, UUID parameterId, String parameterName, BigDecimal percentageThreshold, BigDecimal absoluteThreshold, Integer timeWindowHours, Boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String updatedBy) {
        this.id = id;
        this.branchId = branchId;
        this.parameterId = parameterId;
        this.parameterName = parameterName;
        this.percentageThreshold = percentageThreshold;
        this.absoluteThreshold = absoluteThreshold;
        this.timeWindowHours = timeWindowHours;
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
        DeltaCheckConfigResponse that = (DeltaCheckConfigResponse) o;
        return java.util.Objects.equals(this.id, that.id) &&
               java.util.Objects.equals(this.branchId, that.branchId) &&
               java.util.Objects.equals(this.parameterId, that.parameterId) &&
               java.util.Objects.equals(this.parameterName, that.parameterName) &&
               java.util.Objects.equals(this.percentageThreshold, that.percentageThreshold) &&
               java.util.Objects.equals(this.absoluteThreshold, that.absoluteThreshold) &&
               java.util.Objects.equals(this.timeWindowHours, that.timeWindowHours) &&
               java.util.Objects.equals(this.isActive, that.isActive) &&
               java.util.Objects.equals(this.createdAt, that.createdAt) &&
               java.util.Objects.equals(this.updatedAt, that.updatedAt) &&
               java.util.Objects.equals(this.createdBy, that.createdBy) &&
               java.util.Objects.equals(this.updatedBy, that.updatedBy);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.id, this.branchId, this.parameterId, this.parameterName, this.percentageThreshold, this.absoluteThreshold, this.timeWindowHours, this.isActive, this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
    }

    @Override
    public String toString() {
        return "DeltaCheckConfigResponse{id=" + id +
               ", branchId=" + branchId +
               ", parameterId=" + parameterId +
               ", parameterName=" + parameterName +
               ", percentageThreshold=" + percentageThreshold +
               ", absoluteThreshold=" + absoluteThreshold +
               ", timeWindowHours=" + timeWindowHours +
               ", isActive=" + isActive +
               ", createdAt=" + createdAt +
               ", updatedAt=" + updatedAt +
               ", createdBy=" + createdBy +
               ", updatedBy=" + updatedBy +
               "}";
    }

    public static DeltaCheckConfigResponseBuilder builder() {
        return new DeltaCheckConfigResponseBuilder();
    }

    public static class DeltaCheckConfigResponseBuilder {
        private UUID id;
        private UUID branchId;
        private UUID parameterId;
        private String parameterName;
        private BigDecimal percentageThreshold;
        private BigDecimal absoluteThreshold;
        private Integer timeWindowHours;
        private Boolean isActive;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String createdBy;
        private String updatedBy;

        DeltaCheckConfigResponseBuilder() {
        }

        public DeltaCheckConfigResponseBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public DeltaCheckConfigResponseBuilder branchId(UUID branchId) {
            this.branchId = branchId;
            return this;
        }

        public DeltaCheckConfigResponseBuilder parameterId(UUID parameterId) {
            this.parameterId = parameterId;
            return this;
        }

        public DeltaCheckConfigResponseBuilder parameterName(String parameterName) {
            this.parameterName = parameterName;
            return this;
        }

        public DeltaCheckConfigResponseBuilder percentageThreshold(BigDecimal percentageThreshold) {
            this.percentageThreshold = percentageThreshold;
            return this;
        }

        public DeltaCheckConfigResponseBuilder absoluteThreshold(BigDecimal absoluteThreshold) {
            this.absoluteThreshold = absoluteThreshold;
            return this;
        }

        public DeltaCheckConfigResponseBuilder timeWindowHours(Integer timeWindowHours) {
            this.timeWindowHours = timeWindowHours;
            return this;
        }

        public DeltaCheckConfigResponseBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public DeltaCheckConfigResponseBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public DeltaCheckConfigResponseBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public DeltaCheckConfigResponseBuilder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public DeltaCheckConfigResponseBuilder updatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public DeltaCheckConfigResponse build() {
            return new DeltaCheckConfigResponse(this.id, this.branchId, this.parameterId, this.parameterName, this.percentageThreshold, this.absoluteThreshold, this.timeWindowHours, this.isActive, this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
        }
    }

}
