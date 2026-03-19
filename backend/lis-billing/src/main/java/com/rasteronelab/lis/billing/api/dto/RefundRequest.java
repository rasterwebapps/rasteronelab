package com.rasteronelab.lis.billing.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Request DTO for requesting a Refund.
 */
public class RefundRequest {

    @NotNull(message = "Invoice ID is required")
    private UUID invoiceId;

    private UUID paymentId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    @NotBlank(message = "Reason is required")
    private String reason;

    private String refundMethod;

    public RefundRequest() {
    }

    public RefundRequest(UUID invoiceId, UUID paymentId, BigDecimal amount, String reason,
                         String refundMethod) {
        this.invoiceId = invoiceId;
        this.paymentId = paymentId;
        this.amount = amount;
        this.reason = reason;
        this.refundMethod = refundMethod;
    }

    public UUID getInvoiceId() {
        return this.invoiceId;
    }

    public void setInvoiceId(UUID invoiceId) {
        this.invoiceId = invoiceId;
    }

    public UUID getPaymentId() {
        return this.paymentId;
    }

    public void setPaymentId(UUID paymentId) {
        this.paymentId = paymentId;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRefundMethod() {
        return this.refundMethod;
    }

    public void setRefundMethod(String refundMethod) {
        this.refundMethod = refundMethod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RefundRequest that = (RefundRequest) o;
        return java.util.Objects.equals(this.invoiceId, that.invoiceId) &&
               java.util.Objects.equals(this.amount, that.amount) &&
               java.util.Objects.equals(this.reason, that.reason);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.invoiceId, this.amount, this.reason);
    }

    @Override
    public String toString() {
        return "RefundRequest{invoiceId=" + invoiceId +
               ", amount=" + amount +
               ", reason=" + reason +
               "}";
    }

    public static RefundRequestBuilder builder() {
        return new RefundRequestBuilder();
    }

    public static class RefundRequestBuilder {
        private UUID invoiceId;
        private UUID paymentId;
        private BigDecimal amount;
        private String reason;
        private String refundMethod;

        RefundRequestBuilder() {
        }

        public RefundRequestBuilder invoiceId(UUID invoiceId) {
            this.invoiceId = invoiceId;
            return this;
        }

        public RefundRequestBuilder paymentId(UUID paymentId) {
            this.paymentId = paymentId;
            return this;
        }

        public RefundRequestBuilder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public RefundRequestBuilder reason(String reason) {
            this.reason = reason;
            return this;
        }

        public RefundRequestBuilder refundMethod(String refundMethod) {
            this.refundMethod = refundMethod;
            return this;
        }

        public RefundRequest build() {
            return new RefundRequest(this.invoiceId, this.paymentId, this.amount,
                    this.reason, this.refundMethod);
        }
    }
}
