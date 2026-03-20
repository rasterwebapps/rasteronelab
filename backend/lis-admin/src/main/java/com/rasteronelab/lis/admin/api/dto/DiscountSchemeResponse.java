package com.rasteronelab.lis.admin.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for DiscountScheme entity.
 * Includes all entity fields plus audit metadata.
 */
public class DiscountSchemeResponse {

    private UUID id;
    private UUID branchId;
    private String schemeCode;
    private String schemeName;
    private String applicableTo;
    private String discountType;
    private BigDecimal discountValue;
    private BigDecimal minTransactionAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    public DiscountSchemeResponse() {
    }

    public DiscountSchemeResponse(UUID id, UUID branchId, String schemeCode, String schemeName, String applicableTo, String discountType, BigDecimal discountValue, BigDecimal minTransactionAmount, LocalDate startDate, LocalDate endDate, Boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String updatedBy) {
        this.id = id;
        this.branchId = branchId;
        this.schemeCode = schemeCode;
        this.schemeName = schemeName;
        this.applicableTo = applicableTo;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.minTransactionAmount = minTransactionAmount;
        this.startDate = startDate;
        this.endDate = endDate;
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

    public String getSchemeCode() {
        return this.schemeCode;
    }

    public String getSchemeName() {
        return this.schemeName;
    }

    public String getApplicableTo() {
        return this.applicableTo;
    }

    public String getDiscountType() {
        return this.discountType;
    }

    public BigDecimal getDiscountValue() {
        return this.discountValue;
    }

    public BigDecimal getMinTransactionAmount() {
        return this.minTransactionAmount;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
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

    public void setSchemeCode(String schemeCode) {
        this.schemeCode = schemeCode;
    }

    public void setSchemeName(String schemeName) {
        this.schemeName = schemeName;
    }

    public void setApplicableTo(String applicableTo) {
        this.applicableTo = applicableTo;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public void setMinTransactionAmount(BigDecimal minTransactionAmount) {
        this.minTransactionAmount = minTransactionAmount;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
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
        DiscountSchemeResponse that = (DiscountSchemeResponse) o;
        return java.util.Objects.equals(this.id, that.id) &&
               java.util.Objects.equals(this.branchId, that.branchId) &&
               java.util.Objects.equals(this.schemeCode, that.schemeCode) &&
               java.util.Objects.equals(this.schemeName, that.schemeName) &&
               java.util.Objects.equals(this.applicableTo, that.applicableTo) &&
               java.util.Objects.equals(this.discountType, that.discountType) &&
               java.util.Objects.equals(this.discountValue, that.discountValue) &&
               java.util.Objects.equals(this.minTransactionAmount, that.minTransactionAmount) &&
               java.util.Objects.equals(this.startDate, that.startDate) &&
               java.util.Objects.equals(this.endDate, that.endDate) &&
               java.util.Objects.equals(this.isActive, that.isActive) &&
               java.util.Objects.equals(this.createdAt, that.createdAt) &&
               java.util.Objects.equals(this.updatedAt, that.updatedAt) &&
               java.util.Objects.equals(this.createdBy, that.createdBy) &&
               java.util.Objects.equals(this.updatedBy, that.updatedBy);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.id, this.branchId, this.schemeCode, this.schemeName, this.applicableTo, this.discountType, this.discountValue, this.minTransactionAmount, this.startDate, this.endDate, this.isActive, this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
    }

    @Override
    public String toString() {
        return "DiscountSchemeResponse{id=" + id +
               ", branchId=" + branchId +
               ", schemeCode=" + schemeCode +
               ", schemeName=" + schemeName +
               ", applicableTo=" + applicableTo +
               ", discountType=" + discountType +
               ", discountValue=" + discountValue +
               ", minTransactionAmount=" + minTransactionAmount +
               ", startDate=" + startDate +
               ", endDate=" + endDate +
               ", isActive=" + isActive +
               ", createdAt=" + createdAt +
               ", updatedAt=" + updatedAt +
               ", createdBy=" + createdBy +
               ", updatedBy=" + updatedBy +
               "}";
    }

    public static DiscountSchemeResponseBuilder builder() {
        return new DiscountSchemeResponseBuilder();
    }

    public static class DiscountSchemeResponseBuilder {
        private UUID id;
        private UUID branchId;
        private String schemeCode;
        private String schemeName;
        private String applicableTo;
        private String discountType;
        private BigDecimal discountValue;
        private BigDecimal minTransactionAmount;
        private LocalDate startDate;
        private LocalDate endDate;
        private Boolean isActive;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String createdBy;
        private String updatedBy;

        DiscountSchemeResponseBuilder() {
        }

        public DiscountSchemeResponseBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public DiscountSchemeResponseBuilder branchId(UUID branchId) {
            this.branchId = branchId;
            return this;
        }

        public DiscountSchemeResponseBuilder schemeCode(String schemeCode) {
            this.schemeCode = schemeCode;
            return this;
        }

        public DiscountSchemeResponseBuilder schemeName(String schemeName) {
            this.schemeName = schemeName;
            return this;
        }

        public DiscountSchemeResponseBuilder applicableTo(String applicableTo) {
            this.applicableTo = applicableTo;
            return this;
        }

        public DiscountSchemeResponseBuilder discountType(String discountType) {
            this.discountType = discountType;
            return this;
        }

        public DiscountSchemeResponseBuilder discountValue(BigDecimal discountValue) {
            this.discountValue = discountValue;
            return this;
        }

        public DiscountSchemeResponseBuilder minTransactionAmount(BigDecimal minTransactionAmount) {
            this.minTransactionAmount = minTransactionAmount;
            return this;
        }

        public DiscountSchemeResponseBuilder startDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public DiscountSchemeResponseBuilder endDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }

        public DiscountSchemeResponseBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public DiscountSchemeResponseBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public DiscountSchemeResponseBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public DiscountSchemeResponseBuilder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public DiscountSchemeResponseBuilder updatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public DiscountSchemeResponse build() {
            return new DiscountSchemeResponse(this.id, this.branchId, this.schemeCode, this.schemeName, this.applicableTo, this.discountType, this.discountValue, this.minTransactionAmount, this.startDate, this.endDate, this.isActive, this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
        }
    }

}
