package com.rasteronelab.lis.admin.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for ReferenceRange entity.
 * Includes parameterName for display purposes.
 */
public class ReferenceRangeResponse {

    private UUID id;
    private UUID branchId;
    private UUID parameterId;
    private String parameterName;
    private String gender;
    private BigDecimal ageMin;
    private BigDecimal ageMax;
    private String ageUnit;
    private BigDecimal normalMin;
    private BigDecimal normalMax;
    private BigDecimal criticalLow;
    private BigDecimal criticalHigh;
    private String normalText;
    private String unit;
    private Boolean isPregnancy;
    private String displayText;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    public ReferenceRangeResponse() {
    }

    public ReferenceRangeResponse(UUID id, UUID branchId, UUID parameterId, String parameterName, String gender, BigDecimal ageMin, BigDecimal ageMax, String ageUnit, BigDecimal normalMin, BigDecimal normalMax, BigDecimal criticalLow, BigDecimal criticalHigh, String normalText, String unit, Boolean isPregnancy, String displayText, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String updatedBy) {
        this.id = id;
        this.branchId = branchId;
        this.parameterId = parameterId;
        this.parameterName = parameterName;
        this.gender = gender;
        this.ageMin = ageMin;
        this.ageMax = ageMax;
        this.ageUnit = ageUnit;
        this.normalMin = normalMin;
        this.normalMax = normalMax;
        this.criticalLow = criticalLow;
        this.criticalHigh = criticalHigh;
        this.normalText = normalText;
        this.unit = unit;
        this.isPregnancy = isPregnancy;
        this.displayText = displayText;
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
        ReferenceRangeResponse that = (ReferenceRangeResponse) o;
        return java.util.Objects.equals(this.id, that.id) &&
               java.util.Objects.equals(this.branchId, that.branchId) &&
               java.util.Objects.equals(this.parameterId, that.parameterId) &&
               java.util.Objects.equals(this.parameterName, that.parameterName) &&
               java.util.Objects.equals(this.gender, that.gender) &&
               java.util.Objects.equals(this.ageMin, that.ageMin) &&
               java.util.Objects.equals(this.ageMax, that.ageMax) &&
               java.util.Objects.equals(this.ageUnit, that.ageUnit) &&
               java.util.Objects.equals(this.normalMin, that.normalMin) &&
               java.util.Objects.equals(this.normalMax, that.normalMax) &&
               java.util.Objects.equals(this.criticalLow, that.criticalLow) &&
               java.util.Objects.equals(this.criticalHigh, that.criticalHigh) &&
               java.util.Objects.equals(this.normalText, that.normalText) &&
               java.util.Objects.equals(this.unit, that.unit) &&
               java.util.Objects.equals(this.isPregnancy, that.isPregnancy) &&
               java.util.Objects.equals(this.displayText, that.displayText) &&
               java.util.Objects.equals(this.createdAt, that.createdAt) &&
               java.util.Objects.equals(this.updatedAt, that.updatedAt) &&
               java.util.Objects.equals(this.createdBy, that.createdBy) &&
               java.util.Objects.equals(this.updatedBy, that.updatedBy);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.id, this.branchId, this.parameterId, this.parameterName, this.gender, this.ageMin, this.ageMax, this.ageUnit, this.normalMin, this.normalMax, this.criticalLow, this.criticalHigh, this.normalText, this.unit, this.isPregnancy, this.displayText, this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
    }

    @Override
    public String toString() {
        return "ReferenceRangeResponse{id=" + id +
               ", branchId=" + branchId +
               ", parameterId=" + parameterId +
               ", parameterName=" + parameterName +
               ", gender=" + gender +
               ", ageMin=" + ageMin +
               ", ageMax=" + ageMax +
               ", ageUnit=" + ageUnit +
               ", normalMin=" + normalMin +
               ", normalMax=" + normalMax +
               ", criticalLow=" + criticalLow +
               ", criticalHigh=" + criticalHigh +
               ", normalText=" + normalText +
               ", unit=" + unit +
               ", isPregnancy=" + isPregnancy +
               ", displayText=" + displayText +
               ", createdAt=" + createdAt +
               ", updatedAt=" + updatedAt +
               ", createdBy=" + createdBy +
               ", updatedBy=" + updatedBy +
               "}";
    }

    public static ReferenceRangeResponseBuilder builder() {
        return new ReferenceRangeResponseBuilder();
    }

    public static class ReferenceRangeResponseBuilder {
        private UUID id;
        private UUID branchId;
        private UUID parameterId;
        private String parameterName;
        private String gender;
        private BigDecimal ageMin;
        private BigDecimal ageMax;
        private String ageUnit;
        private BigDecimal normalMin;
        private BigDecimal normalMax;
        private BigDecimal criticalLow;
        private BigDecimal criticalHigh;
        private String normalText;
        private String unit;
        private Boolean isPregnancy;
        private String displayText;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String createdBy;
        private String updatedBy;

        ReferenceRangeResponseBuilder() {
        }

        public ReferenceRangeResponseBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public ReferenceRangeResponseBuilder branchId(UUID branchId) {
            this.branchId = branchId;
            return this;
        }

        public ReferenceRangeResponseBuilder parameterId(UUID parameterId) {
            this.parameterId = parameterId;
            return this;
        }

        public ReferenceRangeResponseBuilder parameterName(String parameterName) {
            this.parameterName = parameterName;
            return this;
        }

        public ReferenceRangeResponseBuilder gender(String gender) {
            this.gender = gender;
            return this;
        }

        public ReferenceRangeResponseBuilder ageMin(BigDecimal ageMin) {
            this.ageMin = ageMin;
            return this;
        }

        public ReferenceRangeResponseBuilder ageMax(BigDecimal ageMax) {
            this.ageMax = ageMax;
            return this;
        }

        public ReferenceRangeResponseBuilder ageUnit(String ageUnit) {
            this.ageUnit = ageUnit;
            return this;
        }

        public ReferenceRangeResponseBuilder normalMin(BigDecimal normalMin) {
            this.normalMin = normalMin;
            return this;
        }

        public ReferenceRangeResponseBuilder normalMax(BigDecimal normalMax) {
            this.normalMax = normalMax;
            return this;
        }

        public ReferenceRangeResponseBuilder criticalLow(BigDecimal criticalLow) {
            this.criticalLow = criticalLow;
            return this;
        }

        public ReferenceRangeResponseBuilder criticalHigh(BigDecimal criticalHigh) {
            this.criticalHigh = criticalHigh;
            return this;
        }

        public ReferenceRangeResponseBuilder normalText(String normalText) {
            this.normalText = normalText;
            return this;
        }

        public ReferenceRangeResponseBuilder unit(String unit) {
            this.unit = unit;
            return this;
        }

        public ReferenceRangeResponseBuilder isPregnancy(Boolean isPregnancy) {
            this.isPregnancy = isPregnancy;
            return this;
        }

        public ReferenceRangeResponseBuilder displayText(String displayText) {
            this.displayText = displayText;
            return this;
        }

        public ReferenceRangeResponseBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public ReferenceRangeResponseBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public ReferenceRangeResponseBuilder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public ReferenceRangeResponseBuilder updatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public ReferenceRangeResponse build() {
            return new ReferenceRangeResponse(this.id, this.branchId, this.parameterId, this.parameterName, this.gender, this.ageMin, this.ageMax, this.ageUnit, this.normalMin, this.normalMax, this.criticalLow, this.criticalHigh, this.normalText, this.unit, this.isPregnancy, this.displayText, this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
        }
    }

}
