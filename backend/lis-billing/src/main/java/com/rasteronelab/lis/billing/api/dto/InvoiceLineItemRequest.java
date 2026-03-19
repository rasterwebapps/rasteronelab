package com.rasteronelab.lis.billing.api.dto;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Request DTO for an invoice line item.
 */
public class InvoiceLineItemRequest {

    private UUID orderLineItemId;

    private UUID testId;

    private String testCode;

    private String testName;

    private Integer quantity;

    private BigDecimal unitPrice;

    private BigDecimal discountAmount;

    public InvoiceLineItemRequest() {
    }

    public InvoiceLineItemRequest(UUID orderLineItemId, UUID testId, String testCode,
                                  String testName, Integer quantity, BigDecimal unitPrice,
                                  BigDecimal discountAmount) {
        this.orderLineItemId = orderLineItemId;
        this.testId = testId;
        this.testCode = testCode;
        this.testName = testName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.discountAmount = discountAmount;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvoiceLineItemRequest that = (InvoiceLineItemRequest) o;
        return java.util.Objects.equals(this.orderLineItemId, that.orderLineItemId) &&
               java.util.Objects.equals(this.testId, that.testId) &&
               java.util.Objects.equals(this.testCode, that.testCode) &&
               java.util.Objects.equals(this.unitPrice, that.unitPrice);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.orderLineItemId, this.testId, this.testCode, this.unitPrice);
    }

    @Override
    public String toString() {
        return "InvoiceLineItemRequest{testCode=" + testCode +
               ", testName=" + testName +
               ", quantity=" + quantity +
               ", unitPrice=" + unitPrice +
               "}";
    }

    public static InvoiceLineItemRequestBuilder builder() {
        return new InvoiceLineItemRequestBuilder();
    }

    public static class InvoiceLineItemRequestBuilder {
        private UUID orderLineItemId;
        private UUID testId;
        private String testCode;
        private String testName;
        private Integer quantity;
        private BigDecimal unitPrice;
        private BigDecimal discountAmount;

        InvoiceLineItemRequestBuilder() {
        }

        public InvoiceLineItemRequestBuilder orderLineItemId(UUID orderLineItemId) {
            this.orderLineItemId = orderLineItemId;
            return this;
        }

        public InvoiceLineItemRequestBuilder testId(UUID testId) {
            this.testId = testId;
            return this;
        }

        public InvoiceLineItemRequestBuilder testCode(String testCode) {
            this.testCode = testCode;
            return this;
        }

        public InvoiceLineItemRequestBuilder testName(String testName) {
            this.testName = testName;
            return this;
        }

        public InvoiceLineItemRequestBuilder quantity(Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        public InvoiceLineItemRequestBuilder unitPrice(BigDecimal unitPrice) {
            this.unitPrice = unitPrice;
            return this;
        }

        public InvoiceLineItemRequestBuilder discountAmount(BigDecimal discountAmount) {
            this.discountAmount = discountAmount;
            return this;
        }

        public InvoiceLineItemRequest build() {
            return new InvoiceLineItemRequest(this.orderLineItemId, this.testId, this.testCode,
                    this.testName, this.quantity, this.unitPrice, this.discountAmount);
        }
    }
}
