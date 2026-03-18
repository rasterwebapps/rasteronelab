package com.rasteronelab.lis.admin.api.dto;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for NumberSeries entity.
 */
public class NumberSeriesResponse {

    private UUID id;
    private UUID branchId;
    private String entityType;
    private String prefix;
    private String suffix;
    private Long currentNumber;
    private Integer paddingLength;
    private String formatPattern;
    private String resetFrequency;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    public NumberSeriesResponse() {
    }

    public NumberSeriesResponse(UUID id, UUID branchId, String entityType, String prefix, String suffix, Long currentNumber, Integer paddingLength, String formatPattern, String resetFrequency, Boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String updatedBy) {
        this.id = id;
        this.branchId = branchId;
        this.entityType = entityType;
        this.prefix = prefix;
        this.suffix = suffix;
        this.currentNumber = currentNumber;
        this.paddingLength = paddingLength;
        this.formatPattern = formatPattern;
        this.resetFrequency = resetFrequency;
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
        NumberSeriesResponse that = (NumberSeriesResponse) o;
        return java.util.Objects.equals(this.id, that.id) &&
               java.util.Objects.equals(this.branchId, that.branchId) &&
               java.util.Objects.equals(this.entityType, that.entityType) &&
               java.util.Objects.equals(this.prefix, that.prefix) &&
               java.util.Objects.equals(this.suffix, that.suffix) &&
               java.util.Objects.equals(this.currentNumber, that.currentNumber) &&
               java.util.Objects.equals(this.paddingLength, that.paddingLength) &&
               java.util.Objects.equals(this.formatPattern, that.formatPattern) &&
               java.util.Objects.equals(this.resetFrequency, that.resetFrequency) &&
               java.util.Objects.equals(this.isActive, that.isActive) &&
               java.util.Objects.equals(this.createdAt, that.createdAt) &&
               java.util.Objects.equals(this.updatedAt, that.updatedAt) &&
               java.util.Objects.equals(this.createdBy, that.createdBy) &&
               java.util.Objects.equals(this.updatedBy, that.updatedBy);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.id, this.branchId, this.entityType, this.prefix, this.suffix, this.currentNumber, this.paddingLength, this.formatPattern, this.resetFrequency, this.isActive, this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
    }

    @Override
    public String toString() {
        return "NumberSeriesResponse{id=" + id +
               ", branchId=" + branchId +
               ", entityType=" + entityType +
               ", prefix=" + prefix +
               ", suffix=" + suffix +
               ", currentNumber=" + currentNumber +
               ", paddingLength=" + paddingLength +
               ", formatPattern=" + formatPattern +
               ", resetFrequency=" + resetFrequency +
               ", isActive=" + isActive +
               ", createdAt=" + createdAt +
               ", updatedAt=" + updatedAt +
               ", createdBy=" + createdBy +
               ", updatedBy=" + updatedBy +
               "}";
    }

    public static NumberSeriesResponseBuilder builder() {
        return new NumberSeriesResponseBuilder();
    }

    public static class NumberSeriesResponseBuilder {
        private UUID id;
        private UUID branchId;
        private String entityType;
        private String prefix;
        private String suffix;
        private Long currentNumber;
        private Integer paddingLength;
        private String formatPattern;
        private String resetFrequency;
        private Boolean isActive;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String createdBy;
        private String updatedBy;

        NumberSeriesResponseBuilder() {
        }

        public NumberSeriesResponseBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public NumberSeriesResponseBuilder branchId(UUID branchId) {
            this.branchId = branchId;
            return this;
        }

        public NumberSeriesResponseBuilder entityType(String entityType) {
            this.entityType = entityType;
            return this;
        }

        public NumberSeriesResponseBuilder prefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public NumberSeriesResponseBuilder suffix(String suffix) {
            this.suffix = suffix;
            return this;
        }

        public NumberSeriesResponseBuilder currentNumber(Long currentNumber) {
            this.currentNumber = currentNumber;
            return this;
        }

        public NumberSeriesResponseBuilder paddingLength(Integer paddingLength) {
            this.paddingLength = paddingLength;
            return this;
        }

        public NumberSeriesResponseBuilder formatPattern(String formatPattern) {
            this.formatPattern = formatPattern;
            return this;
        }

        public NumberSeriesResponseBuilder resetFrequency(String resetFrequency) {
            this.resetFrequency = resetFrequency;
            return this;
        }

        public NumberSeriesResponseBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public NumberSeriesResponseBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public NumberSeriesResponseBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public NumberSeriesResponseBuilder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public NumberSeriesResponseBuilder updatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public NumberSeriesResponse build() {
            return new NumberSeriesResponse(this.id, this.branchId, this.entityType, this.prefix, this.suffix, this.currentNumber, this.paddingLength, this.formatPattern, this.resetFrequency, this.isActive, this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
        }
    }

}
