package com.rasteronelab.lis.admin.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Request DTO for creating or updating a Price Catalog entry.
 * Either testId or panelId must be provided, but not both.
 */
public class PriceCatalogRequest {

    private UUID testId;

    private UUID panelId;

    @NotBlank(message = "Rate list type is required")
    @Size(max = 30, message = "Rate list type must not exceed 30 characters")
    private String rateListType;

    @NotNull(message = "Price is required")
    private BigDecimal price;

    private LocalDate effectiveFrom;

    private LocalDate effectiveTo;

    private Boolean isActive;

    public PriceCatalogRequest() {
    }

    public PriceCatalogRequest(UUID testId, UUID panelId, String rateListType, BigDecimal price, LocalDate effectiveFrom, LocalDate effectiveTo, Boolean isActive) {
        this.testId = testId;
        this.panelId = panelId;
        this.rateListType = rateListType;
        this.price = price;
        this.effectiveFrom = effectiveFrom;
        this.effectiveTo = effectiveTo;
        this.isActive = isActive;
    }

    public UUID getTestId() {
        return this.testId;
    }

    public UUID getPanelId() {
        return this.panelId;
    }

    public String getRateListType() {
        return this.rateListType;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public LocalDate getEffectiveFrom() {
        return this.effectiveFrom;
    }

    public LocalDate getEffectiveTo() {
        return this.effectiveTo;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public void setTestId(UUID testId) {
        this.testId = testId;
    }

    public void setPanelId(UUID panelId) {
        this.panelId = panelId;
    }

    public void setRateListType(String rateListType) {
        this.rateListType = rateListType;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setEffectiveFrom(LocalDate effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    public void setEffectiveTo(LocalDate effectiveTo) {
        this.effectiveTo = effectiveTo;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PriceCatalogRequest that = (PriceCatalogRequest) o;
        return java.util.Objects.equals(this.testId, that.testId) &&
               java.util.Objects.equals(this.panelId, that.panelId) &&
               java.util.Objects.equals(this.rateListType, that.rateListType) &&
               java.util.Objects.equals(this.price, that.price) &&
               java.util.Objects.equals(this.effectiveFrom, that.effectiveFrom) &&
               java.util.Objects.equals(this.effectiveTo, that.effectiveTo) &&
               java.util.Objects.equals(this.isActive, that.isActive);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.testId, this.panelId, this.rateListType, this.price, this.effectiveFrom, this.effectiveTo, this.isActive);
    }

    @Override
    public String toString() {
        return "PriceCatalogRequest{testId=" + testId +
               ", panelId=" + panelId +
               ", rateListType=" + rateListType +
               ", price=" + price +
               ", effectiveFrom=" + effectiveFrom +
               ", effectiveTo=" + effectiveTo +
               ", isActive=" + isActive +
               "}";
    }

    public static PriceCatalogRequestBuilder builder() {
        return new PriceCatalogRequestBuilder();
    }

    public static class PriceCatalogRequestBuilder {
        private UUID testId;
        private UUID panelId;
        private String rateListType;
        private BigDecimal price;
        private LocalDate effectiveFrom;
        private LocalDate effectiveTo;
        private Boolean isActive;

        PriceCatalogRequestBuilder() {
        }

        public PriceCatalogRequestBuilder testId(UUID testId) {
            this.testId = testId;
            return this;
        }

        public PriceCatalogRequestBuilder panelId(UUID panelId) {
            this.panelId = panelId;
            return this;
        }

        public PriceCatalogRequestBuilder rateListType(String rateListType) {
            this.rateListType = rateListType;
            return this;
        }

        public PriceCatalogRequestBuilder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public PriceCatalogRequestBuilder effectiveFrom(LocalDate effectiveFrom) {
            this.effectiveFrom = effectiveFrom;
            return this;
        }

        public PriceCatalogRequestBuilder effectiveTo(LocalDate effectiveTo) {
            this.effectiveTo = effectiveTo;
            return this;
        }

        public PriceCatalogRequestBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public PriceCatalogRequest build() {
            return new PriceCatalogRequest(this.testId, this.panelId, this.rateListType, this.price, this.effectiveFrom, this.effectiveTo, this.isActive);
        }
    }

}
