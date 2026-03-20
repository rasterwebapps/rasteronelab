package com.rasteronelab.lis.admin.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for PriceCatalog entity.
 * Includes resolved testName and panelName for display.
 */
public class PriceCatalogResponse {

    private UUID id;
    private UUID branchId;
    private UUID testId;
    private String testName;
    private UUID panelId;
    private String panelName;
    private String rateListType;
    private BigDecimal price;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    public PriceCatalogResponse() {
    }

    public PriceCatalogResponse(UUID id, UUID branchId, UUID testId, String testName, UUID panelId, String panelName, String rateListType, BigDecimal price, LocalDate effectiveFrom, LocalDate effectiveTo, Boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String updatedBy) {
        this.id = id;
        this.branchId = branchId;
        this.testId = testId;
        this.testName = testName;
        this.panelId = panelId;
        this.panelName = panelName;
        this.rateListType = rateListType;
        this.price = price;
        this.effectiveFrom = effectiveFrom;
        this.effectiveTo = effectiveTo;
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

    public UUID getTestId() {
        return this.testId;
    }

    public String getTestName() {
        return this.testName;
    }

    public UUID getPanelId() {
        return this.panelId;
    }

    public String getPanelName() {
        return this.panelName;
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

    public void setTestId(UUID testId) {
        this.testId = testId;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public void setPanelId(UUID panelId) {
        this.panelId = panelId;
    }

    public void setPanelName(String panelName) {
        this.panelName = panelName;
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
        PriceCatalogResponse that = (PriceCatalogResponse) o;
        return java.util.Objects.equals(this.id, that.id) &&
               java.util.Objects.equals(this.branchId, that.branchId) &&
               java.util.Objects.equals(this.testId, that.testId) &&
               java.util.Objects.equals(this.testName, that.testName) &&
               java.util.Objects.equals(this.panelId, that.panelId) &&
               java.util.Objects.equals(this.panelName, that.panelName) &&
               java.util.Objects.equals(this.rateListType, that.rateListType) &&
               java.util.Objects.equals(this.price, that.price) &&
               java.util.Objects.equals(this.effectiveFrom, that.effectiveFrom) &&
               java.util.Objects.equals(this.effectiveTo, that.effectiveTo) &&
               java.util.Objects.equals(this.isActive, that.isActive) &&
               java.util.Objects.equals(this.createdAt, that.createdAt) &&
               java.util.Objects.equals(this.updatedAt, that.updatedAt) &&
               java.util.Objects.equals(this.createdBy, that.createdBy) &&
               java.util.Objects.equals(this.updatedBy, that.updatedBy);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.id, this.branchId, this.testId, this.testName, this.panelId, this.panelName, this.rateListType, this.price, this.effectiveFrom, this.effectiveTo, this.isActive, this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
    }

    @Override
    public String toString() {
        return "PriceCatalogResponse{id=" + id +
               ", branchId=" + branchId +
               ", testId=" + testId +
               ", testName=" + testName +
               ", panelId=" + panelId +
               ", panelName=" + panelName +
               ", rateListType=" + rateListType +
               ", price=" + price +
               ", effectiveFrom=" + effectiveFrom +
               ", effectiveTo=" + effectiveTo +
               ", isActive=" + isActive +
               ", createdAt=" + createdAt +
               ", updatedAt=" + updatedAt +
               ", createdBy=" + createdBy +
               ", updatedBy=" + updatedBy +
               "}";
    }

    public static PriceCatalogResponseBuilder builder() {
        return new PriceCatalogResponseBuilder();
    }

    public static class PriceCatalogResponseBuilder {
        private UUID id;
        private UUID branchId;
        private UUID testId;
        private String testName;
        private UUID panelId;
        private String panelName;
        private String rateListType;
        private BigDecimal price;
        private LocalDate effectiveFrom;
        private LocalDate effectiveTo;
        private Boolean isActive;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String createdBy;
        private String updatedBy;

        PriceCatalogResponseBuilder() {
        }

        public PriceCatalogResponseBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public PriceCatalogResponseBuilder branchId(UUID branchId) {
            this.branchId = branchId;
            return this;
        }

        public PriceCatalogResponseBuilder testId(UUID testId) {
            this.testId = testId;
            return this;
        }

        public PriceCatalogResponseBuilder testName(String testName) {
            this.testName = testName;
            return this;
        }

        public PriceCatalogResponseBuilder panelId(UUID panelId) {
            this.panelId = panelId;
            return this;
        }

        public PriceCatalogResponseBuilder panelName(String panelName) {
            this.panelName = panelName;
            return this;
        }

        public PriceCatalogResponseBuilder rateListType(String rateListType) {
            this.rateListType = rateListType;
            return this;
        }

        public PriceCatalogResponseBuilder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public PriceCatalogResponseBuilder effectiveFrom(LocalDate effectiveFrom) {
            this.effectiveFrom = effectiveFrom;
            return this;
        }

        public PriceCatalogResponseBuilder effectiveTo(LocalDate effectiveTo) {
            this.effectiveTo = effectiveTo;
            return this;
        }

        public PriceCatalogResponseBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public PriceCatalogResponseBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public PriceCatalogResponseBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public PriceCatalogResponseBuilder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public PriceCatalogResponseBuilder updatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public PriceCatalogResponse build() {
            return new PriceCatalogResponse(this.id, this.branchId, this.testId, this.testName, this.panelId, this.panelName, this.rateListType, this.price, this.effectiveFrom, this.effectiveTo, this.isActive, this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
        }
    }

}
