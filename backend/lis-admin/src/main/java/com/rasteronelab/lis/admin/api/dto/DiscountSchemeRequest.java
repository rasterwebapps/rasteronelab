package com.rasteronelab.lis.admin.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Request DTO for creating or updating a DiscountScheme.
 */
public class DiscountSchemeRequest {

    @NotBlank(message = "Scheme code is required")
    @Size(max = 50, message = "Scheme code must not exceed 50 characters")
    private String schemeCode;

    @NotBlank(message = "Scheme name is required")
    @Size(max = 200, message = "Scheme name must not exceed 200 characters")
    private String schemeName;

    @Size(max = 30, message = "Applicable to must not exceed 30 characters")
    private String applicableTo;

    @Size(max = 30, message = "Discount type must not exceed 30 characters")
    private String discountType;

    @NotNull(message = "Discount value is required")
    @DecimalMin(value = "0", message = "Discount value must be at least 0")
    private BigDecimal discountValue;

    private BigDecimal minTransactionAmount;

    private LocalDate startDate;

    private LocalDate endDate;

    private Boolean isActive;

    public DiscountSchemeRequest() {
    }

    public DiscountSchemeRequest(String schemeCode, String schemeName, String applicableTo, String discountType, BigDecimal discountValue, BigDecimal minTransactionAmount, LocalDate startDate, LocalDate endDate, Boolean isActive) {
        this.schemeCode = schemeCode;
        this.schemeName = schemeName;
        this.applicableTo = applicableTo;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.minTransactionAmount = minTransactionAmount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isActive = isActive;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiscountSchemeRequest that = (DiscountSchemeRequest) o;
        return java.util.Objects.equals(this.schemeCode, that.schemeCode) &&
               java.util.Objects.equals(this.schemeName, that.schemeName) &&
               java.util.Objects.equals(this.applicableTo, that.applicableTo) &&
               java.util.Objects.equals(this.discountType, that.discountType) &&
               java.util.Objects.equals(this.discountValue, that.discountValue) &&
               java.util.Objects.equals(this.minTransactionAmount, that.minTransactionAmount) &&
               java.util.Objects.equals(this.startDate, that.startDate) &&
               java.util.Objects.equals(this.endDate, that.endDate) &&
               java.util.Objects.equals(this.isActive, that.isActive);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.schemeCode, this.schemeName, this.applicableTo, this.discountType, this.discountValue, this.minTransactionAmount, this.startDate, this.endDate, this.isActive);
    }

    @Override
    public String toString() {
        return "DiscountSchemeRequest{schemeCode=" + schemeCode +
               ", schemeName=" + schemeName +
               ", applicableTo=" + applicableTo +
               ", discountType=" + discountType +
               ", discountValue=" + discountValue +
               ", minTransactionAmount=" + minTransactionAmount +
               ", startDate=" + startDate +
               ", endDate=" + endDate +
               ", isActive=" + isActive +
               "}";
    }

    public static DiscountSchemeRequestBuilder builder() {
        return new DiscountSchemeRequestBuilder();
    }

    public static class DiscountSchemeRequestBuilder {
        private String schemeCode;
        private String schemeName;
        private String applicableTo;
        private String discountType;
        private BigDecimal discountValue;
        private BigDecimal minTransactionAmount;
        private LocalDate startDate;
        private LocalDate endDate;
        private Boolean isActive;

        DiscountSchemeRequestBuilder() {
        }

        public DiscountSchemeRequestBuilder schemeCode(String schemeCode) {
            this.schemeCode = schemeCode;
            return this;
        }

        public DiscountSchemeRequestBuilder schemeName(String schemeName) {
            this.schemeName = schemeName;
            return this;
        }

        public DiscountSchemeRequestBuilder applicableTo(String applicableTo) {
            this.applicableTo = applicableTo;
            return this;
        }

        public DiscountSchemeRequestBuilder discountType(String discountType) {
            this.discountType = discountType;
            return this;
        }

        public DiscountSchemeRequestBuilder discountValue(BigDecimal discountValue) {
            this.discountValue = discountValue;
            return this;
        }

        public DiscountSchemeRequestBuilder minTransactionAmount(BigDecimal minTransactionAmount) {
            this.minTransactionAmount = minTransactionAmount;
            return this;
        }

        public DiscountSchemeRequestBuilder startDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public DiscountSchemeRequestBuilder endDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }

        public DiscountSchemeRequestBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public DiscountSchemeRequest build() {
            return new DiscountSchemeRequest(this.schemeCode, this.schemeName, this.applicableTo, this.discountType, this.discountValue, this.minTransactionAmount, this.startDate, this.endDate, this.isActive);
        }
    }

}
