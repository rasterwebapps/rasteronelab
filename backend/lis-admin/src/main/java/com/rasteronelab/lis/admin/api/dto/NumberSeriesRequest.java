package com.rasteronelab.lis.admin.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating or updating a Number Series.
 */
public class NumberSeriesRequest {

    @NotBlank(message = "Entity type is required")
    @Size(max = 30, message = "Entity type must not exceed 30 characters")
    private String entityType;

    @NotBlank(message = "Prefix is required")
    @Size(max = 20, message = "Prefix must not exceed 20 characters")
    private String prefix;

    @Size(max = 20, message = "Suffix must not exceed 20 characters")
    private String suffix;

    @NotNull(message = "Current number is required")
    private Long currentNumber;

    private Integer paddingLength;

    @Size(max = 100, message = "Format pattern must not exceed 100 characters")
    private String formatPattern;

    @Size(max = 20, message = "Reset frequency must not exceed 20 characters")
    private String resetFrequency;

    private Boolean isActive;

    public NumberSeriesRequest() {
    }

    public NumberSeriesRequest(String entityType, String prefix, String suffix, Long currentNumber, Integer paddingLength, String formatPattern, String resetFrequency, Boolean isActive) {
        this.entityType = entityType;
        this.prefix = prefix;
        this.suffix = suffix;
        this.currentNumber = currentNumber;
        this.paddingLength = paddingLength;
        this.formatPattern = formatPattern;
        this.resetFrequency = resetFrequency;
        this.isActive = isActive;
    }

    public String getEntityType() {
        return this.entityType;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public String getSuffix() {
        return this.suffix;
    }

    public Long getCurrentNumber() {
        return this.currentNumber;
    }

    public Integer getPaddingLength() {
        return this.paddingLength;
    }

    public String getFormatPattern() {
        return this.formatPattern;
    }

    public String getResetFrequency() {
        return this.resetFrequency;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public void setCurrentNumber(Long currentNumber) {
        this.currentNumber = currentNumber;
    }

    public void setPaddingLength(Integer paddingLength) {
        this.paddingLength = paddingLength;
    }

    public void setFormatPattern(String formatPattern) {
        this.formatPattern = formatPattern;
    }

    public void setResetFrequency(String resetFrequency) {
        this.resetFrequency = resetFrequency;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NumberSeriesRequest that = (NumberSeriesRequest) o;
        return java.util.Objects.equals(this.entityType, that.entityType) &&
               java.util.Objects.equals(this.prefix, that.prefix) &&
               java.util.Objects.equals(this.suffix, that.suffix) &&
               java.util.Objects.equals(this.currentNumber, that.currentNumber) &&
               java.util.Objects.equals(this.paddingLength, that.paddingLength) &&
               java.util.Objects.equals(this.formatPattern, that.formatPattern) &&
               java.util.Objects.equals(this.resetFrequency, that.resetFrequency) &&
               java.util.Objects.equals(this.isActive, that.isActive);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.entityType, this.prefix, this.suffix, this.currentNumber, this.paddingLength, this.formatPattern, this.resetFrequency, this.isActive);
    }

    @Override
    public String toString() {
        return "NumberSeriesRequest{entityType=" + entityType +
               ", prefix=" + prefix +
               ", suffix=" + suffix +
               ", currentNumber=" + currentNumber +
               ", paddingLength=" + paddingLength +
               ", formatPattern=" + formatPattern +
               ", resetFrequency=" + resetFrequency +
               ", isActive=" + isActive +
               "}";
    }

    public static NumberSeriesRequestBuilder builder() {
        return new NumberSeriesRequestBuilder();
    }

    public static class NumberSeriesRequestBuilder {
        private String entityType;
        private String prefix;
        private String suffix;
        private Long currentNumber;
        private Integer paddingLength;
        private String formatPattern;
        private String resetFrequency;
        private Boolean isActive;

        NumberSeriesRequestBuilder() {
        }

        public NumberSeriesRequestBuilder entityType(String entityType) {
            this.entityType = entityType;
            return this;
        }

        public NumberSeriesRequestBuilder prefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public NumberSeriesRequestBuilder suffix(String suffix) {
            this.suffix = suffix;
            return this;
        }

        public NumberSeriesRequestBuilder currentNumber(Long currentNumber) {
            this.currentNumber = currentNumber;
            return this;
        }

        public NumberSeriesRequestBuilder paddingLength(Integer paddingLength) {
            this.paddingLength = paddingLength;
            return this;
        }

        public NumberSeriesRequestBuilder formatPattern(String formatPattern) {
            this.formatPattern = formatPattern;
            return this;
        }

        public NumberSeriesRequestBuilder resetFrequency(String resetFrequency) {
            this.resetFrequency = resetFrequency;
            return this;
        }

        public NumberSeriesRequestBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public NumberSeriesRequest build() {
            return new NumberSeriesRequest(this.entityType, this.prefix, this.suffix, this.currentNumber, this.paddingLength, this.formatPattern, this.resetFrequency, this.isActive);
        }
    }

}
