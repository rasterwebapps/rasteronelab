package com.rasteronelab.lis.billing.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for InvoiceLineItem entity.
 */
public class InvoiceLineItemResponse {

    private UUID id;
    private UUID branchId;
    private UUID invoiceId;
    private UUID orderLineItemId;
    private UUID testId;
    private String testCode;
    private String testName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal discountAmount;
    private BigDecimal netAmount;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    public InvoiceLineItemResponse() {
    }

    public InvoiceLineItemResponse(UUID id, UUID branchId, UUID invoiceId, UUID orderLineItemId,
                                   UUID testId, String testCode, String testName, Integer quantity,
                                   BigDecimal unitPrice, BigDecimal discountAmount, BigDecimal netAmount,
                                   Boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt,
                                   String createdBy, String updatedBy) {
        this.id = id;
        this.branchId = branchId;
        this.invoiceId = invoiceId;
        this.orderLineItemId = orderLineItemId;
        this.testId = testId;
        this.testCode = testCode;
        this.testName = testName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.discountAmount = discountAmount;
        this.netAmount = netAmount;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getBranchId() {
        return this.branchId;
    }

    public void setBranchId(UUID branchId) {
        this.branchId = branchId;
    }

    public UUID getInvoiceId() {
        return this.invoiceId;
    }

    public void setInvoiceId(UUID invoiceId) {
        this.invoiceId = invoiceId;
    }

    public UUID getOrderLineItemId() {
        return this.orderLineItemId;
    }

    public void setOrderLineItemId(UUID orderLineItemId) {
        this.orderLineItemId = orderLineItemId;
    }

    public UUID getTestId() {
        return this.testId;
    }

    public void setTestId(UUID testId) {
        this.testId = testId;
    }

    public String getTestCode() {
        return this.testCode;
    }

    public void setTestCode(String testCode) {
        this.testCode = testCode;
    }

    public String getTestName() {
        return this.testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return this.unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getDiscountAmount() {
        return this.discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getNetAmount() {
        return this.netAmount;
    }

    public void setNetAmount(BigDecimal netAmount) {
        this.netAmount = netAmount;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvoiceLineItemResponse that = (InvoiceLineItemResponse) o;
        return java.util.Objects.equals(this.id, that.id) &&
               java.util.Objects.equals(this.invoiceId, that.invoiceId) &&
               java.util.Objects.equals(this.testCode, that.testCode);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.id, this.invoiceId, this.testCode);
    }

    @Override
    public String toString() {
        return "InvoiceLineItemResponse{id=" + id +
               ", testCode=" + testCode +
               ", testName=" + testName +
               ", netAmount=" + netAmount +
               "}";
    }

    public static InvoiceLineItemResponseBuilder builder() {
        return new InvoiceLineItemResponseBuilder();
    }

    public static class InvoiceLineItemResponseBuilder {
        private UUID id;
        private UUID branchId;
        private UUID invoiceId;
        private UUID orderLineItemId;
        private UUID testId;
        private String testCode;
        private String testName;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal discountAmount;
        private BigDecimal netAmount;
        private Boolean isActive;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String createdBy;
        private String updatedBy;

        InvoiceLineItemResponseBuilder() {
        }

        public InvoiceLineItemResponseBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public InvoiceLineItemResponseBuilder branchId(UUID branchId) {
            this.branchId = branchId;
            return this;
        }

        public InvoiceLineItemResponseBuilder invoiceId(UUID invoiceId) {
            this.invoiceId = invoiceId;
            return this;
        }

        public InvoiceLineItemResponseBuilder orderLineItemId(UUID orderLineItemId) {
            this.orderLineItemId = orderLineItemId;
            return this;
        }

        public InvoiceLineItemResponseBuilder testId(UUID testId) {
            this.testId = testId;
            return this;
        }

        public InvoiceLineItemResponseBuilder testCode(String testCode) {
            this.testCode = testCode;
            return this;
        }

        public InvoiceLineItemResponseBuilder testName(String testName) {
            this.testName = testName;
            return this;
        }

        public InvoiceLineItemResponseBuilder quantity(Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        public InvoiceLineItemResponseBuilder unitPrice(BigDecimal unitPrice) {
            this.unitPrice = unitPrice;
            return this;
        }

        public InvoiceLineItemResponseBuilder discountAmount(BigDecimal discountAmount) {
            this.discountAmount = discountAmount;
            return this;
        }

        public InvoiceLineItemResponseBuilder netAmount(BigDecimal netAmount) {
            this.netAmount = netAmount;
            return this;
        }

        public InvoiceLineItemResponseBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public InvoiceLineItemResponseBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public InvoiceLineItemResponseBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public InvoiceLineItemResponseBuilder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public InvoiceLineItemResponseBuilder updatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public InvoiceLineItemResponse build() {
            return new InvoiceLineItemResponse(this.id, this.branchId, this.invoiceId,
                    this.orderLineItemId, this.testId, this.testCode, this.testName,
                    this.quantity, this.unitPrice, this.discountAmount, this.netAmount,
                    this.isActive, this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
        }
    }
}
