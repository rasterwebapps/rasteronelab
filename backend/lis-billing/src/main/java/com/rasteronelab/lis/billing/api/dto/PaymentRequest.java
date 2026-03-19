package com.rasteronelab.lis.billing.api.dto;

import com.rasteronelab.lis.billing.domain.model.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Request DTO for recording a Payment.
 */
public class PaymentRequest {

    @NotNull(message = "Invoice ID is required")
    private UUID invoiceId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    private String transactionRef;

    private String notes;

    public PaymentRequest() {
    }

    public PaymentRequest(UUID invoiceId, BigDecimal amount, PaymentMethod paymentMethod,
                          String transactionRef, String notes) {
        this.invoiceId = invoiceId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.transactionRef = transactionRef;
        this.notes = notes;
    }

    public UUID getInvoiceId() {
        return this.invoiceId;
    }

    public void setInvoiceId(UUID invoiceId) {
        this.invoiceId = invoiceId;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentMethod getPaymentMethod() {
        return this.paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTransactionRef() {
        return this.transactionRef;
    }

    public void setTransactionRef(String transactionRef) {
        this.transactionRef = transactionRef;
    }

    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentRequest that = (PaymentRequest) o;
        return java.util.Objects.equals(this.invoiceId, that.invoiceId) &&
               java.util.Objects.equals(this.amount, that.amount) &&
               java.util.Objects.equals(this.paymentMethod, that.paymentMethod);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.invoiceId, this.amount, this.paymentMethod);
    }

    @Override
    public String toString() {
        return "PaymentRequest{invoiceId=" + invoiceId +
               ", amount=" + amount +
               ", paymentMethod=" + paymentMethod +
               "}";
    }

    public static PaymentRequestBuilder builder() {
        return new PaymentRequestBuilder();
    }

    public static class PaymentRequestBuilder {
        private UUID invoiceId;
        private BigDecimal amount;
        private PaymentMethod paymentMethod;
        private String transactionRef;
        private String notes;

        PaymentRequestBuilder() {
        }

        public PaymentRequestBuilder invoiceId(UUID invoiceId) {
            this.invoiceId = invoiceId;
            return this;
        }

        public PaymentRequestBuilder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public PaymentRequestBuilder paymentMethod(PaymentMethod paymentMethod) {
            this.paymentMethod = paymentMethod;
            return this;
        }

        public PaymentRequestBuilder transactionRef(String transactionRef) {
            this.transactionRef = transactionRef;
            return this;
        }

        public PaymentRequestBuilder notes(String notes) {
            this.notes = notes;
            return this;
        }

        public PaymentRequest build() {
            return new PaymentRequest(this.invoiceId, this.amount, this.paymentMethod,
                    this.transactionRef, this.notes);
        }
    }
}
