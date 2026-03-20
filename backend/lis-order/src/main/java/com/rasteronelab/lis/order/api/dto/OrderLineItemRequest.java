package com.rasteronelab.lis.order.api.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Request DTO for creating an OrderLineItem within a TestOrder.
 */
public class OrderLineItemRequest {

    @NotNull(message = "Test ID is required")
    private UUID testId;

    private String testCode;

    private String testName;

    private UUID departmentId;

    private String sampleType;

    private String tubeType;

    private BigDecimal unitPrice;

    private BigDecimal discountAmount;

    private Boolean isUrgent;

    private Boolean isOutsourced;

    private String outsourceLab;

    private String remarks;

    public OrderLineItemRequest() {
    }

    public OrderLineItemRequest(UUID testId, String testCode, String testName, UUID departmentId, String sampleType, String tubeType, BigDecimal unitPrice, BigDecimal discountAmount, Boolean isUrgent, Boolean isOutsourced, String outsourceLab, String remarks) {
        this.testId = testId;
        this.testCode = testCode;
        this.testName = testName;
        this.departmentId = departmentId;
        this.sampleType = sampleType;
        this.tubeType = tubeType;
        this.unitPrice = unitPrice;
        this.discountAmount = discountAmount;
        this.isUrgent = isUrgent;
        this.isOutsourced = isOutsourced;
        this.outsourceLab = outsourceLab;
        this.remarks = remarks;
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

    public BigDecimal getUnitPrice() {
        return this.unitPrice;
    }

    public BigDecimal getDiscountAmount() {
        return this.discountAmount;
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

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderLineItemRequest that = (OrderLineItemRequest) o;
        return java.util.Objects.equals(this.testId, that.testId) &&
               java.util.Objects.equals(this.testCode, that.testCode) &&
               java.util.Objects.equals(this.testName, that.testName) &&
               java.util.Objects.equals(this.departmentId, that.departmentId) &&
               java.util.Objects.equals(this.sampleType, that.sampleType) &&
               java.util.Objects.equals(this.tubeType, that.tubeType) &&
               java.util.Objects.equals(this.unitPrice, that.unitPrice) &&
               java.util.Objects.equals(this.discountAmount, that.discountAmount) &&
               java.util.Objects.equals(this.isUrgent, that.isUrgent) &&
               java.util.Objects.equals(this.isOutsourced, that.isOutsourced) &&
               java.util.Objects.equals(this.outsourceLab, that.outsourceLab) &&
               java.util.Objects.equals(this.remarks, that.remarks);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.testId, this.testCode, this.testName, this.departmentId, this.sampleType, this.tubeType, this.unitPrice, this.discountAmount, this.isUrgent, this.isOutsourced, this.outsourceLab, this.remarks);
    }

    @Override
    public String toString() {
        return "OrderLineItemRequest{testId=" + testId +
               ", testCode=" + testCode +
               ", testName=" + testName +
               ", departmentId=" + departmentId +
               ", sampleType=" + sampleType +
               ", tubeType=" + tubeType +
               ", unitPrice=" + unitPrice +
               ", discountAmount=" + discountAmount +
               ", isUrgent=" + isUrgent +
               ", isOutsourced=" + isOutsourced +
               ", outsourceLab=" + outsourceLab +
               ", remarks=" + remarks +
               "}";
    }

    public static OrderLineItemRequestBuilder builder() {
        return new OrderLineItemRequestBuilder();
    }

    public static class OrderLineItemRequestBuilder {
        private UUID testId;
        private String testCode;
        private String testName;
        private UUID departmentId;
        private String sampleType;
        private String tubeType;
        private BigDecimal unitPrice;
        private BigDecimal discountAmount;
        private Boolean isUrgent;
        private Boolean isOutsourced;
        private String outsourceLab;
        private String remarks;

        OrderLineItemRequestBuilder() {
        }

        public OrderLineItemRequestBuilder testId(UUID testId) {
            this.testId = testId;
            return this;
        }

        public OrderLineItemRequestBuilder testCode(String testCode) {
            this.testCode = testCode;
            return this;
        }

        public OrderLineItemRequestBuilder testName(String testName) {
            this.testName = testName;
            return this;
        }

        public OrderLineItemRequestBuilder departmentId(UUID departmentId) {
            this.departmentId = departmentId;
            return this;
        }

        public OrderLineItemRequestBuilder sampleType(String sampleType) {
            this.sampleType = sampleType;
            return this;
        }

        public OrderLineItemRequestBuilder tubeType(String tubeType) {
            this.tubeType = tubeType;
            return this;
        }

        public OrderLineItemRequestBuilder unitPrice(BigDecimal unitPrice) {
            this.unitPrice = unitPrice;
            return this;
        }

        public OrderLineItemRequestBuilder discountAmount(BigDecimal discountAmount) {
            this.discountAmount = discountAmount;
            return this;
        }

        public OrderLineItemRequestBuilder isUrgent(Boolean isUrgent) {
            this.isUrgent = isUrgent;
            return this;
        }

        public OrderLineItemRequestBuilder isOutsourced(Boolean isOutsourced) {
            this.isOutsourced = isOutsourced;
            return this;
        }

        public OrderLineItemRequestBuilder outsourceLab(String outsourceLab) {
            this.outsourceLab = outsourceLab;
            return this;
        }

        public OrderLineItemRequestBuilder remarks(String remarks) {
            this.remarks = remarks;
            return this;
        }

        public OrderLineItemRequest build() {
            return new OrderLineItemRequest(this.testId, this.testCode, this.testName, this.departmentId, this.sampleType, this.tubeType, this.unitPrice, this.discountAmount, this.isUrgent, this.isOutsourced, this.outsourceLab, this.remarks);
        }
    }

}
