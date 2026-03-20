package com.rasteronelab.lis.admin.api.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Request DTO for creating or updating a Reference Range.
 */
public class ReferenceRangeRequest {

    @NotNull(message = "Parameter ID is required")
    private UUID parameterId;

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

    public ReferenceRangeRequest() {
    }

    public ReferenceRangeRequest(UUID parameterId, String gender, BigDecimal ageMin, BigDecimal ageMax, String ageUnit, BigDecimal normalMin, BigDecimal normalMax, BigDecimal criticalLow, BigDecimal criticalHigh, String normalText, String unit, Boolean isPregnancy, String displayText) {
        this.parameterId = parameterId;
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
    }

    public UUID getParameterId() {
        return this.parameterId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReferenceRangeRequest that = (ReferenceRangeRequest) o;
        return java.util.Objects.equals(this.parameterId, that.parameterId) &&
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
               java.util.Objects.equals(this.displayText, that.displayText);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.parameterId, this.gender, this.ageMin, this.ageMax, this.ageUnit, this.normalMin, this.normalMax, this.criticalLow, this.criticalHigh, this.normalText, this.unit, this.isPregnancy, this.displayText);
    }

    @Override
    public String toString() {
        return "ReferenceRangeRequest{parameterId=" + parameterId +
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
               "}";
    }

    public static ReferenceRangeRequestBuilder builder() {
        return new ReferenceRangeRequestBuilder();
    }

    public static class ReferenceRangeRequestBuilder {
        private UUID parameterId;
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

        ReferenceRangeRequestBuilder() {
        }

        public ReferenceRangeRequestBuilder parameterId(UUID parameterId) {
            this.parameterId = parameterId;
            return this;
        }

        public ReferenceRangeRequestBuilder gender(String gender) {
            this.gender = gender;
            return this;
        }

        public ReferenceRangeRequestBuilder ageMin(BigDecimal ageMin) {
            this.ageMin = ageMin;
            return this;
        }

        public ReferenceRangeRequestBuilder ageMax(BigDecimal ageMax) {
            this.ageMax = ageMax;
            return this;
        }

        public ReferenceRangeRequestBuilder ageUnit(String ageUnit) {
            this.ageUnit = ageUnit;
            return this;
        }

        public ReferenceRangeRequestBuilder normalMin(BigDecimal normalMin) {
            this.normalMin = normalMin;
            return this;
        }

        public ReferenceRangeRequestBuilder normalMax(BigDecimal normalMax) {
            this.normalMax = normalMax;
            return this;
        }

        public ReferenceRangeRequestBuilder criticalLow(BigDecimal criticalLow) {
            this.criticalLow = criticalLow;
            return this;
        }

        public ReferenceRangeRequestBuilder criticalHigh(BigDecimal criticalHigh) {
            this.criticalHigh = criticalHigh;
            return this;
        }

        public ReferenceRangeRequestBuilder normalText(String normalText) {
            this.normalText = normalText;
            return this;
        }

        public ReferenceRangeRequestBuilder unit(String unit) {
            this.unit = unit;
            return this;
        }

        public ReferenceRangeRequestBuilder isPregnancy(Boolean isPregnancy) {
            this.isPregnancy = isPregnancy;
            return this;
        }

        public ReferenceRangeRequestBuilder displayText(String displayText) {
            this.displayText = displayText;
            return this;
        }

        public ReferenceRangeRequest build() {
            return new ReferenceRangeRequest(this.parameterId, this.gender, this.ageMin, this.ageMax, this.ageUnit, this.normalMin, this.normalMax, this.criticalLow, this.criticalHigh, this.normalText, this.unit, this.isPregnancy, this.displayText);
        }
    }

}
