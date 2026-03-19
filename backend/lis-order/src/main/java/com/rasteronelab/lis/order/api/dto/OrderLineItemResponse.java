package com.rasteronelab.lis.order.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for OrderLineItem entity.
 * Includes all entity fields plus audit metadata.
 */
public class OrderLineItemResponse {

    private UUID id;
    private UUID branchId;
    private UUID orderId;
    private UUID testId;
    private String testCode;
    private String testName;
    private UUID departmentId;
    private String sampleType;
    private String tubeType;
    private String status;
    private BigDecimal unitPrice;
    private BigDecimal discountAmount;
    private BigDecimal netPrice;
    private Boolean isUrgent;
    private Boolean isOutsourced;
    private String outsourceLab;
    private String remarks;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    public OrderLineItemResponse() {
    }

    public OrderLineItemResponse(UUID id, UUID branchId, UUID orderId, UUID testId, String testCode, String testName, UUID departmentId, String sampleType, String tubeType, String status, BigDecimal unitPrice, BigDecimal discountAmount, BigDecimal netPrice, Boolean isUrgent, Boolean isOutsourced, String outsourceLab, String remarks, Boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String updatedBy) {
        this.id = id;
        this.branchId = branchId;
        this.orderId = orderId;
        this.testId = testId;
        this.testCode = testCode;
        this.testName = testName;
        this.departmentId = departmentId;
        this.sampleType = sampleType;
        this.tubeType = tubeType;
        this.status = status;
        this.unitPrice = unitPrice;
        this.discountAmount = discountAmount;
        this.netPrice = netPrice;
        this.isUrgent = isUrgent;
        this.isOutsourced = isOutsourced;
        this.outsourceLab = outsourceLab;
        this.remarks = remarks;
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

    public UUID getOrderId() {
        return this.orderId;
    }

    public UUID getTestId() {
        return this.testId;
    }

    public String getTestCode() {
        return this.testCode;
    }

    public String getTestName() {
        return this.testName;
    }

    public UUID getDepartmentId() {
        return this.departmentId;
    }

    public String getSampleType() {
        return this.sampleType;
    }

    public String getTubeType() {
        return this.tubeType;
    }

    public String getStatus() {
        return this.status;
    }

    public BigDecimal getUnitPrice() {
        return this.unitPrice;
    }

    public BigDecimal getDiscountAmount() {
        return this.discountAmount;
    }

    public BigDecimal getNetPrice() {
        return this.netPrice;
    }

    public Boolean getIsUrgent() {
        return this.isUrgent;
    }

    public Boolean getIsOutsourced() {
        return this.isOutsourced;
    }

    public String getOutsourceLab() {
        return this.outsourceLab;
    }

    public String getRemarks() {
        return this.remarks;
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

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public void setTestId(UUID testId) {
        this.testId = testId;
    }

    public void setTestCode(String testCode) {
        this.testCode = testCode;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public void setDepartmentId(UUID departmentId) {
        this.departmentId = departmentId;
    }

    public void setSampleType(String sampleType) {
        this.sampleType = sampleType;
    }

    public void setTubeType(String tubeType) {
        this.tubeType = tubeType;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public void setNetPrice(BigDecimal netPrice) {
        this.netPrice = netPrice;
    }

    public void setIsUrgent(Boolean isUrgent) {
        this.isUrgent = isUrgent;
    }

    public void setIsOutsourced(Boolean isOutsourced) {
        this.isOutsourced = isOutsourced;
    }

    public void setOutsourceLab(String outsourceLab) {
        this.outsourceLab = outsourceLab;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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
        OrderLineItemResponse that = (OrderLineItemResponse) o;
        return java.util.Objects.equals(this.id, that.id) &&
               java.util.Objects.equals(this.branchId, that.branchId) &&
               java.util.Objects.equals(this.orderId, that.orderId) &&
               java.util.Objects.equals(this.testId, that.testId) &&
               java.util.Objects.equals(this.testCode, that.testCode) &&
               java.util.Objects.equals(this.testName, that.testName) &&
               java.util.Objects.equals(this.departmentId, that.departmentId) &&
               java.util.Objects.equals(this.sampleType, that.sampleType) &&
               java.util.Objects.equals(this.tubeType, that.tubeType) &&
               java.util.Objects.equals(this.status, that.status) &&
               java.util.Objects.equals(this.unitPrice, that.unitPrice) &&
               java.util.Objects.equals(this.discountAmount, that.discountAmount) &&
               java.util.Objects.equals(this.netPrice, that.netPrice) &&
               java.util.Objects.equals(this.isUrgent, that.isUrgent) &&
               java.util.Objects.equals(this.isOutsourced, that.isOutsourced) &&
               java.util.Objects.equals(this.outsourceLab, that.outsourceLab) &&
               java.util.Objects.equals(this.remarks, that.remarks) &&
               java.util.Objects.equals(this.isActive, that.isActive) &&
               java.util.Objects.equals(this.createdAt, that.createdAt) &&
               java.util.Objects.equals(this.updatedAt, that.updatedAt) &&
               java.util.Objects.equals(this.createdBy, that.createdBy) &&
               java.util.Objects.equals(this.updatedBy, that.updatedBy);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.id, this.branchId, this.orderId, this.testId, this.testCode, this.testName, this.departmentId, this.sampleType, this.tubeType, this.status, this.unitPrice, this.discountAmount, this.netPrice, this.isUrgent, this.isOutsourced, this.outsourceLab, this.remarks, this.isActive, this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
    }

    @Override
    public String toString() {
        return "OrderLineItemResponse{id=" + id +
               ", branchId=" + branchId +
               ", orderId=" + orderId +
               ", testId=" + testId +
               ", testCode=" + testCode +
               ", testName=" + testName +
               ", departmentId=" + departmentId +
               ", sampleType=" + sampleType +
               ", tubeType=" + tubeType +
               ", status=" + status +
               ", unitPrice=" + unitPrice +
               ", discountAmount=" + discountAmount +
               ", netPrice=" + netPrice +
               ", isUrgent=" + isUrgent +
               ", isOutsourced=" + isOutsourced +
               ", outsourceLab=" + outsourceLab +
               ", remarks=" + remarks +
               ", isActive=" + isActive +
               ", createdAt=" + createdAt +
               ", updatedAt=" + updatedAt +
               ", createdBy=" + createdBy +
               ", updatedBy=" + updatedBy +
               "}";
    }

    public static OrderLineItemResponseBuilder builder() {
        return new OrderLineItemResponseBuilder();
    }

    public static class OrderLineItemResponseBuilder {
        private UUID id;
        private UUID branchId;
        private UUID orderId;
        private UUID testId;
        private String testCode;
        private String testName;
        private UUID departmentId;
        private String sampleType;
        private String tubeType;
        private String status;
        private BigDecimal unitPrice;
        private BigDecimal discountAmount;
        private BigDecimal netPrice;
        private Boolean isUrgent;
        private Boolean isOutsourced;
        private String outsourceLab;
        private String remarks;
        private Boolean isActive;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String createdBy;
        private String updatedBy;

        OrderLineItemResponseBuilder() {
        }

        public OrderLineItemResponseBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public OrderLineItemResponseBuilder branchId(UUID branchId) {
            this.branchId = branchId;
            return this;
        }

        public OrderLineItemResponseBuilder orderId(UUID orderId) {
            this.orderId = orderId;
            return this;
        }

        public OrderLineItemResponseBuilder testId(UUID testId) {
            this.testId = testId;
            return this;
        }

        public OrderLineItemResponseBuilder testCode(String testCode) {
            this.testCode = testCode;
            return this;
        }

        public OrderLineItemResponseBuilder testName(String testName) {
            this.testName = testName;
            return this;
        }

        public OrderLineItemResponseBuilder departmentId(UUID departmentId) {
            this.departmentId = departmentId;
            return this;
        }

        public OrderLineItemResponseBuilder sampleType(String sampleType) {
            this.sampleType = sampleType;
            return this;
        }

        public OrderLineItemResponseBuilder tubeType(String tubeType) {
            this.tubeType = tubeType;
            return this;
        }

        public OrderLineItemResponseBuilder status(String status) {
            this.status = status;
            return this;
        }

        public OrderLineItemResponseBuilder unitPrice(BigDecimal unitPrice) {
            this.unitPrice = unitPrice;
            return this;
        }

        public OrderLineItemResponseBuilder discountAmount(BigDecimal discountAmount) {
            this.discountAmount = discountAmount;
            return this;
        }

        public OrderLineItemResponseBuilder netPrice(BigDecimal netPrice) {
            this.netPrice = netPrice;
            return this;
        }

        public OrderLineItemResponseBuilder isUrgent(Boolean isUrgent) {
            this.isUrgent = isUrgent;
            return this;
        }

        public OrderLineItemResponseBuilder isOutsourced(Boolean isOutsourced) {
            this.isOutsourced = isOutsourced;
            return this;
        }

        public OrderLineItemResponseBuilder outsourceLab(String outsourceLab) {
            this.outsourceLab = outsourceLab;
            return this;
        }

        public OrderLineItemResponseBuilder remarks(String remarks) {
            this.remarks = remarks;
            return this;
        }

        public OrderLineItemResponseBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public OrderLineItemResponseBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public OrderLineItemResponseBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public OrderLineItemResponseBuilder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public OrderLineItemResponseBuilder updatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public OrderLineItemResponse build() {
            return new OrderLineItemResponse(this.id, this.branchId, this.orderId, this.testId, this.testCode, this.testName, this.departmentId, this.sampleType, this.tubeType, this.status, this.unitPrice, this.discountAmount, this.netPrice, this.isUrgent, this.isOutsourced, this.outsourceLab, this.remarks, this.isActive, this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
        }
    }

}
