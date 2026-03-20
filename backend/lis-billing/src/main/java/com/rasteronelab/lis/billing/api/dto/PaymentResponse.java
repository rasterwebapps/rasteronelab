package com.rasteronelab.lis.billing.api.dto;

import com.rasteronelab.lis.billing.domain.model.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for Payment entity.
 * Includes all payment fields plus invoiceNumber and audit metadata.
 */
public class PaymentResponse {

    private UUID id;
    private UUID branchId;
    private UUID invoiceId;
    private String invoiceNumber;
    private String receiptNumber;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private String transactionRef;
    private LocalDateTime paymentDate;
    private String receivedBy;
    private String notes;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    public PaymentResponse() {
    }

    public PaymentResponse(UUID id, UUID branchId, UUID invoiceId, String invoiceNumber,
                           String receiptNumber, BigDecimal amount, PaymentMethod paymentMethod,
                           String transactionRef, LocalDateTime paymentDate, String receivedBy,
                           String notes, Boolean isActive, LocalDateTime createdAt,
                           LocalDateTime updatedAt, String createdBy, String updatedBy) {
        this.id = id;
        this.branchId = branchId;
        this.invoiceId = invoiceId;
        this.invoiceNumber = invoiceNumber;
        this.receiptNumber = receiptNumber;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.transactionRef = transactionRef;
        this.paymentDate = paymentDate;
        this.receivedBy = receivedBy;
        this.notes = notes;
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

    public String getInvoiceNumber() {
        return this.invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getReceiptNumber() {
        return this.receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
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

    public LocalDateTime getPaymentDate() {
        return this.paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getReceivedBy() {
        return this.receivedBy;
    }

    public void setReceivedBy(String receivedBy) {
        this.receivedBy = receivedBy;
    }

    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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
        PaymentResponse that = (PaymentResponse) o;
        return java.util.Objects.equals(this.id, that.id) &&
               java.util.Objects.equals(this.receiptNumber, that.receiptNumber) &&
               java.util.Objects.equals(this.branchId, that.branchId);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.id, this.receiptNumber, this.branchId);
    }

    @Override
    public String toString() {
        return "PaymentResponse{id=" + id +
               ", receiptNumber=" + receiptNumber +
               ", amount=" + amount +
               ", paymentMethod=" + paymentMethod +
               "}";
    }

    public static PaymentResponseBuilder builder() {
        return new PaymentResponseBuilder();
    }

    public static class PaymentResponseBuilder {
        private UUID id;
        private UUID branchId;
        private UUID invoiceId;
        private String invoiceNumber;
        private String receiptNumber;
        private BigDecimal amount;
        private PaymentMethod paymentMethod;
        private String transactionRef;
        private LocalDateTime paymentDate;
        private String receivedBy;
        private String notes;
        private Boolean isActive;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String createdBy;
        private String updatedBy;

        PaymentResponseBuilder() {
        }

        public PaymentResponseBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public PaymentResponseBuilder branchId(UUID branchId) {
            this.branchId = branchId;
            return this;
        }

        public PaymentResponseBuilder invoiceId(UUID invoiceId) {
            this.invoiceId = invoiceId;
            return this;
        }

        public PaymentResponseBuilder invoiceNumber(String invoiceNumber) {
            this.invoiceNumber = invoiceNumber;
            return this;
        }

        public PaymentResponseBuilder receiptNumber(String receiptNumber) {
            this.receiptNumber = receiptNumber;
            return this;
        }

        public PaymentResponseBuilder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public PaymentResponseBuilder paymentMethod(PaymentMethod paymentMethod) {
            this.paymentMethod = paymentMethod;
            return this;
        }

        public PaymentResponseBuilder transactionRef(String transactionRef) {
            this.transactionRef = transactionRef;
            return this;
        }

        public PaymentResponseBuilder paymentDate(LocalDateTime paymentDate) {
            this.paymentDate = paymentDate;
            return this;
        }

        public PaymentResponseBuilder receivedBy(String receivedBy) {
            this.receivedBy = receivedBy;
            return this;
        }

        public PaymentResponseBuilder notes(String notes) {
            this.notes = notes;
            return this;
        }

        public PaymentResponseBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public PaymentResponseBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public PaymentResponseBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public PaymentResponseBuilder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public PaymentResponseBuilder updatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public PaymentResponse build() {
            return new PaymentResponse(this.id, this.branchId, this.invoiceId, this.invoiceNumber,
                    this.receiptNumber, this.amount, this.paymentMethod, this.transactionRef,
                    this.paymentDate, this.receivedBy, this.notes, this.isActive,
                    this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
        }
    }
}
