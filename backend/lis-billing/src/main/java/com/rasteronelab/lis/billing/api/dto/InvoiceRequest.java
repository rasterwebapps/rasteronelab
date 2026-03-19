package com.rasteronelab.lis.billing.api.dto;

import com.rasteronelab.lis.billing.domain.model.RateListType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Request DTO for creating an Invoice.
 */
public class InvoiceRequest {

    @NotNull(message = "Order ID is required")
    private UUID orderId;

    @NotNull(message = "Patient ID is required")
    private UUID patientId;

    private RateListType rateListType;

    private String discountType;

    private BigDecimal discountAmount;

    private String discountReason;

    private String notes;

    @Valid
    private List<InvoiceLineItemRequest> lineItems = new ArrayList<>();

    public InvoiceRequest() {
    }

    public InvoiceRequest(UUID orderId, UUID patientId, RateListType rateListType,
                          String discountType, BigDecimal discountAmount, String discountReason,
                          String notes, List<InvoiceLineItemRequest> lineItems) {
        this.orderId = orderId;
        this.patientId = patientId;
        this.rateListType = rateListType;
        this.discountType = discountType;
        this.discountAmount = discountAmount;
        this.discountReason = discountReason;
        this.notes = notes;
        this.lineItems = lineItems;
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

    public String getDiscountType() {
        return this.discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getDiscountAmount() {
        return this.discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getDiscountReason() {
        return this.discountReason;
    }

    public void setDiscountReason(String discountReason) {
        this.discountReason = discountReason;
    }

    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<InvoiceLineItemRequest> getLineItems() {
        return this.lineItems;
    }

    public void setLineItems(List<InvoiceLineItemRequest> lineItems) {
        this.lineItems = lineItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvoiceRequest that = (InvoiceRequest) o;
        return java.util.Objects.equals(this.orderId, that.orderId) &&
               java.util.Objects.equals(this.patientId, that.patientId) &&
               java.util.Objects.equals(this.rateListType, that.rateListType) &&
               java.util.Objects.equals(this.discountType, that.discountType) &&
               java.util.Objects.equals(this.discountAmount, that.discountAmount) &&
               java.util.Objects.equals(this.notes, that.notes);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.orderId, this.patientId, this.rateListType,
                this.discountType, this.discountAmount, this.notes);
    }

    @Override
    public String toString() {
        return "InvoiceRequest{orderId=" + orderId +
               ", patientId=" + patientId +
               ", rateListType=" + rateListType +
               "}";
    }

    public static InvoiceRequestBuilder builder() {
        return new InvoiceRequestBuilder();
    }

    public static class InvoiceRequestBuilder {
        private UUID orderId;
        private UUID patientId;
        private RateListType rateListType;
        private String discountType;
        private BigDecimal discountAmount;
        private String discountReason;
        private String notes;
        private List<InvoiceLineItemRequest> lineItems = new ArrayList<>();

        InvoiceRequestBuilder() {
        }

        public InvoiceRequestBuilder orderId(UUID orderId) {
            this.orderId = orderId;
            return this;
        }

        public InvoiceRequestBuilder patientId(UUID patientId) {
            this.patientId = patientId;
            return this;
        }

        public InvoiceRequestBuilder rateListType(RateListType rateListType) {
            this.rateListType = rateListType;
            return this;
        }

        public InvoiceRequestBuilder discountType(String discountType) {
            this.discountType = discountType;
            return this;
        }

        public InvoiceRequestBuilder discountAmount(BigDecimal discountAmount) {
            this.discountAmount = discountAmount;
            return this;
        }

        public InvoiceRequestBuilder discountReason(String discountReason) {
            this.discountReason = discountReason;
            return this;
        }

        public InvoiceRequestBuilder notes(String notes) {
            this.notes = notes;
            return this;
        }

        public InvoiceRequestBuilder lineItems(List<InvoiceLineItemRequest> lineItems) {
            this.lineItems = lineItems;
            return this;
        }

        public InvoiceRequest build() {
            return new InvoiceRequest(this.orderId, this.patientId, this.rateListType,
                    this.discountType, this.discountAmount, this.discountReason,
                    this.notes, this.lineItems);
        }
    }
}
