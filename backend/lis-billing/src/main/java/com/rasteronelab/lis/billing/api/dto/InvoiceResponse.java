package com.rasteronelab.lis.billing.api.dto;

import com.rasteronelab.lis.billing.domain.model.InvoiceStatus;
import com.rasteronelab.lis.billing.domain.model.RateListType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Response DTO for Invoice entity.
 * Includes all invoice fields plus line items and audit metadata.
 */
public class InvoiceResponse {

    private UUID id;
    private UUID branchId;
    private String invoiceNumber;
    private UUID orderId;
    private UUID patientId;
    private RateListType rateListType;
    private BigDecimal subtotal;
    private BigDecimal discountAmount;
    private String discountType;
    private String discountReason;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
    private BigDecimal balanceAmount;
    private InvoiceStatus status;
    private LocalDate invoiceDate;
    private LocalDate dueDate;
    private String notes;
    private Boolean isActive;
    private List<InvoiceLineItemResponse> lineItems = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    public InvoiceResponse() {
    }

    public InvoiceResponse(UUID id, UUID branchId, String invoiceNumber, UUID orderId,
                           UUID patientId, RateListType rateListType, BigDecimal subtotal,
                           BigDecimal discountAmount, String discountType, String discountReason,
                           BigDecimal taxAmount, BigDecimal totalAmount, BigDecimal paidAmount,
                           BigDecimal balanceAmount, InvoiceStatus status, LocalDate invoiceDate,
                           LocalDate dueDate, String notes, Boolean isActive,
                           List<InvoiceLineItemResponse> lineItems,
                           LocalDateTime createdAt, LocalDateTime updatedAt,
                           String createdBy, String updatedBy) {
        this.id = id;
        this.branchId = branchId;
        this.invoiceNumber = invoiceNumber;
        this.orderId = orderId;
        this.patientId = patientId;
        this.rateListType = rateListType;
        this.subtotal = subtotal;
        this.discountAmount = discountAmount;
        this.discountType = discountType;
        this.discountReason = discountReason;
        this.taxAmount = taxAmount;
        this.totalAmount = totalAmount;
        this.paidAmount = paidAmount;
        this.balanceAmount = balanceAmount;
        this.status = status;
        this.invoiceDate = invoiceDate;
        this.dueDate = dueDate;
        this.notes = notes;
        this.isActive = isActive;
        this.lineItems = lineItems;
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

    public String getInvoiceNumber() {
        return this.invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public UUID getOrderId() {
        return this.orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public UUID getPatientId() {
        return this.patientId;
    }

    public void setPatientId(UUID patientId) {
        this.patientId = patientId;
    }

    public RateListType getRateListType() {
        return this.rateListType;
    }

    public void setRateListType(RateListType rateListType) {
        this.rateListType = rateListType;
    }

    public BigDecimal getSubtotal() {
        return this.subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getDiscountAmount() {
        return this.discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getDiscountType() {
        return this.discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public String getDiscountReason() {
        return this.discountReason;
    }

    public void setDiscountReason(String discountReason) {
        this.discountReason = discountReason;
    }

    public BigDecimal getTaxAmount() {
        return this.taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getTotalAmount() {
        return this.totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getPaidAmount() {
        return this.paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public BigDecimal getBalanceAmount() {
        return this.balanceAmount;
    }

    public void setBalanceAmount(BigDecimal balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public InvoiceStatus getStatus() {
        return this.status;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }

    public LocalDate getInvoiceDate() {
        return this.invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public LocalDate getDueDate() {
        return this.dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
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

    public List<InvoiceLineItemResponse> getLineItems() {
        return this.lineItems;
    }

    public void setLineItems(List<InvoiceLineItemResponse> lineItems) {
        this.lineItems = lineItems;
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
        InvoiceResponse that = (InvoiceResponse) o;
        return java.util.Objects.equals(this.id, that.id) &&
               java.util.Objects.equals(this.invoiceNumber, that.invoiceNumber) &&
               java.util.Objects.equals(this.branchId, that.branchId);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.id, this.invoiceNumber, this.branchId);
    }

    @Override
    public String toString() {
        return "InvoiceResponse{id=" + id +
               ", invoiceNumber=" + invoiceNumber +
               ", status=" + status +
               ", totalAmount=" + totalAmount +
               "}";
    }

    public static InvoiceResponseBuilder builder() {
        return new InvoiceResponseBuilder();
    }

    public static class InvoiceResponseBuilder {
        private UUID id;
        private UUID branchId;
        private String invoiceNumber;
        private UUID orderId;
        private UUID patientId;
        private RateListType rateListType;
        private BigDecimal subtotal;
        private BigDecimal discountAmount;
        private String discountType;
        private String discountReason;
        private BigDecimal taxAmount;
        private BigDecimal totalAmount;
        private BigDecimal paidAmount;
        private BigDecimal balanceAmount;
        private InvoiceStatus status;
        private LocalDate invoiceDate;
        private LocalDate dueDate;
        private String notes;
        private Boolean isActive;
        private List<InvoiceLineItemResponse> lineItems = new ArrayList<>();
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String createdBy;
        private String updatedBy;

        InvoiceResponseBuilder() {
        }

        public InvoiceResponseBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public InvoiceResponseBuilder branchId(UUID branchId) {
            this.branchId = branchId;
            return this;
        }

        public InvoiceResponseBuilder invoiceNumber(String invoiceNumber) {
            this.invoiceNumber = invoiceNumber;
            return this;
        }

        public InvoiceResponseBuilder orderId(UUID orderId) {
            this.orderId = orderId;
            return this;
        }

        public InvoiceResponseBuilder patientId(UUID patientId) {
            this.patientId = patientId;
            return this;
        }

        public InvoiceResponseBuilder rateListType(RateListType rateListType) {
            this.rateListType = rateListType;
            return this;
        }

        public InvoiceResponseBuilder subtotal(BigDecimal subtotal) {
            this.subtotal = subtotal;
            return this;
        }

        public InvoiceResponseBuilder discountAmount(BigDecimal discountAmount) {
            this.discountAmount = discountAmount;
            return this;
        }

        public InvoiceResponseBuilder discountType(String discountType) {
            this.discountType = discountType;
            return this;
        }

        public InvoiceResponseBuilder discountReason(String discountReason) {
            this.discountReason = discountReason;
            return this;
        }

        public InvoiceResponseBuilder taxAmount(BigDecimal taxAmount) {
            this.taxAmount = taxAmount;
            return this;
        }

        public InvoiceResponseBuilder totalAmount(BigDecimal totalAmount) {
            this.totalAmount = totalAmount;
            return this;
        }

        public InvoiceResponseBuilder paidAmount(BigDecimal paidAmount) {
            this.paidAmount = paidAmount;
            return this;
        }

        public InvoiceResponseBuilder balanceAmount(BigDecimal balanceAmount) {
            this.balanceAmount = balanceAmount;
            return this;
        }

        public InvoiceResponseBuilder status(InvoiceStatus status) {
            this.status = status;
            return this;
        }

        public InvoiceResponseBuilder invoiceDate(LocalDate invoiceDate) {
            this.invoiceDate = invoiceDate;
            return this;
        }

        public InvoiceResponseBuilder dueDate(LocalDate dueDate) {
            this.dueDate = dueDate;
            return this;
        }

        public InvoiceResponseBuilder notes(String notes) {
            this.notes = notes;
            return this;
        }

        public InvoiceResponseBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public InvoiceResponseBuilder lineItems(List<InvoiceLineItemResponse> lineItems) {
            this.lineItems = lineItems;
            return this;
        }

        public InvoiceResponseBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public InvoiceResponseBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public InvoiceResponseBuilder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public InvoiceResponseBuilder updatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public InvoiceResponse build() {
            return new InvoiceResponse(this.id, this.branchId, this.invoiceNumber, this.orderId,
                    this.patientId, this.rateListType, this.subtotal, this.discountAmount,
                    this.discountType, this.discountReason, this.taxAmount, this.totalAmount,
                    this.paidAmount, this.balanceAmount, this.status, this.invoiceDate,
                    this.dueDate, this.notes, this.isActive, this.lineItems,
                    this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
        }
    }
}
