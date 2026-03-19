package com.rasteronelab.lis.billing.api.dto;

import com.rasteronelab.lis.billing.domain.model.RefundStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for Refund entity.
 */
public class RefundResponse {

    private UUID id;
    private UUID branchId;
    private UUID invoiceId;
    private UUID paymentId;
    private String refundNumber;
    private BigDecimal amount;
    private String reason;
    private RefundStatus status;
    private String approvedBy;
    private LocalDateTime approvedAt;
    private String refundMethod;
    private LocalDateTime refundDate;
    private String notes;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    public RefundResponse() {
    }

    public RefundResponse(UUID id, UUID branchId, UUID invoiceId, UUID paymentId,
                          String refundNumber, BigDecimal amount, String reason,
                          RefundStatus status, String approvedBy, LocalDateTime approvedAt,
                          String refundMethod, LocalDateTime refundDate, String notes,
                          Boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt,
                          String createdBy, String updatedBy) {
        this.id = id;
        this.branchId = branchId;
        this.invoiceId = invoiceId;
        this.paymentId = paymentId;
        this.refundNumber = refundNumber;
        this.amount = amount;
        this.reason = reason;
        this.status = status;
        this.approvedBy = approvedBy;
        this.approvedAt = approvedAt;
        this.refundMethod = refundMethod;
        this.refundDate = refundDate;
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

    public UUID getPaymentId() {
        return this.paymentId;
    }

    public void setPaymentId(UUID paymentId) {
        this.paymentId = paymentId;
    }

    public String getRefundNumber() {
        return this.refundNumber;
    }

    public void setRefundNumber(String refundNumber) {
        this.refundNumber = refundNumber;
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

    public RefundStatus getStatus() {
        return this.status;
    }

    public void setStatus(RefundStatus status) {
        this.status = status;
    }

    public String getApprovedBy() {
        return this.approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public LocalDateTime getApprovedAt() {
        return this.approvedAt;
    }

    public void setApprovedAt(LocalDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }

    public String getRefundMethod() {
        return this.refundMethod;
    }

    public void setRefundMethod(String refundMethod) {
        this.refundMethod = refundMethod;
    }

    public LocalDateTime getRefundDate() {
        return this.refundDate;
    }

    public void setRefundDate(LocalDateTime refundDate) {
        this.refundDate = refundDate;
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
        RefundResponse that = (RefundResponse) o;
        return java.util.Objects.equals(this.id, that.id) &&
               java.util.Objects.equals(this.refundNumber, that.refundNumber) &&
               java.util.Objects.equals(this.branchId, that.branchId);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.id, this.refundNumber, this.branchId);
    }

    @Override
    public String toString() {
        return "RefundResponse{id=" + id +
               ", refundNumber=" + refundNumber +
               ", amount=" + amount +
               ", status=" + status +
               "}";
    }

    public static RefundResponseBuilder builder() {
        return new RefundResponseBuilder();
    }

    public static class RefundResponseBuilder {
        private UUID id;
        private UUID branchId;
        private UUID invoiceId;
        private UUID paymentId;
        private String refundNumber;
        private BigDecimal amount;
        private String reason;
        private RefundStatus status;
        private String approvedBy;
        private LocalDateTime approvedAt;
        private String refundMethod;
        private LocalDateTime refundDate;
        private String notes;
        private Boolean isActive;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String createdBy;
        private String updatedBy;

        RefundResponseBuilder() {
        }

        public RefundResponseBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public RefundResponseBuilder branchId(UUID branchId) {
            this.branchId = branchId;
            return this;
        }

        public RefundResponseBuilder invoiceId(UUID invoiceId) {
            this.invoiceId = invoiceId;
            return this;
        }

        public RefundResponseBuilder paymentId(UUID paymentId) {
            this.paymentId = paymentId;
            return this;
        }

        public RefundResponseBuilder refundNumber(String refundNumber) {
            this.refundNumber = refundNumber;
            return this;
        }

        public RefundResponseBuilder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public RefundResponseBuilder reason(String reason) {
            this.reason = reason;
            return this;
        }

        public RefundResponseBuilder status(RefundStatus status) {
            this.status = status;
            return this;
        }

        public RefundResponseBuilder approvedBy(String approvedBy) {
            this.approvedBy = approvedBy;
            return this;
        }

        public RefundResponseBuilder approvedAt(LocalDateTime approvedAt) {
            this.approvedAt = approvedAt;
            return this;
        }

        public RefundResponseBuilder refundMethod(String refundMethod) {
            this.refundMethod = refundMethod;
            return this;
        }

        public RefundResponseBuilder refundDate(LocalDateTime refundDate) {
            this.refundDate = refundDate;
            return this;
        }

        public RefundResponseBuilder notes(String notes) {
            this.notes = notes;
            return this;
        }

        public RefundResponseBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public RefundResponseBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public RefundResponseBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public RefundResponseBuilder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public RefundResponseBuilder updatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public RefundResponse build() {
            return new RefundResponse(this.id, this.branchId, this.invoiceId, this.paymentId,
                    this.refundNumber, this.amount, this.reason, this.status, this.approvedBy,
                    this.approvedAt, this.refundMethod, this.refundDate, this.notes, this.isActive,
                    this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
        }
    }
}
