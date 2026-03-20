package com.rasteronelab.lis.billing.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * Request DTO for applying a discount scheme to an invoice.
 */
public class DiscountApplicationRequest {

    @NotNull(message = "Invoice ID is required")
    private UUID invoiceId;

    @NotNull(message = "Discount type is required")
    private String discountType;

    @NotNull(message = "Discount value is required")
    @Positive(message = "Discount value must be positive")
    private BigDecimal discountValue;

    private String discountReason;

    private String schemeCode;

    private String applicableTo;

    public DiscountApplicationRequest() {
    }

    public DiscountApplicationRequest(UUID invoiceId, String discountType, BigDecimal discountValue,
                                      String discountReason, String schemeCode, String applicableTo) {
        this.invoiceId = invoiceId;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.discountReason = discountReason;
        this.schemeCode = schemeCode;
        this.applicableTo = applicableTo;
    }

    public UUID getInvoiceId() {
        return this.invoiceId;
    }

    public void setInvoiceId(UUID invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getDiscountType() {
        return this.discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getDiscountValue() {
        return this.discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public String getDiscountReason() {
        return this.discountReason;
    }

    public void setDiscountReason(String discountReason) {
        this.discountReason = discountReason;
    }

    public String getSchemeCode() {
        return this.schemeCode;
    }

    public void setSchemeCode(String schemeCode) {
        this.schemeCode = schemeCode;
    }

    public String getApplicableTo() {
        return this.applicableTo;
    }

    public void setApplicableTo(String applicableTo) {
        this.applicableTo = applicableTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiscountApplicationRequest that = (DiscountApplicationRequest) o;
        return Objects.equals(this.invoiceId, that.invoiceId) &&
               Objects.equals(this.discountType, that.discountType) &&
               Objects.equals(this.discountValue, that.discountValue) &&
               Objects.equals(this.schemeCode, that.schemeCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.invoiceId, this.discountType, this.discountValue, this.schemeCode);
    }

    @Override
    public String toString() {
        return "DiscountApplicationRequest{invoiceId=" + invoiceId +
               ", discountType=" + discountType +
               ", discountValue=" + discountValue +
               ", schemeCode=" + schemeCode +
               "}";
    }

    public static DiscountApplicationRequestBuilder builder() {
        return new DiscountApplicationRequestBuilder();
    }

    public static class DiscountApplicationRequestBuilder {
        private UUID invoiceId;
        private String discountType;
        private BigDecimal discountValue;
        private String discountReason;
        private String schemeCode;
        private String applicableTo;

        DiscountApplicationRequestBuilder() {
        }

        public DiscountApplicationRequestBuilder invoiceId(UUID invoiceId) {
            this.invoiceId = invoiceId;
            return this;
        }

        public DiscountApplicationRequestBuilder discountType(String discountType) {
            this.discountType = discountType;
            return this;
        }

        public DiscountApplicationRequestBuilder discountValue(BigDecimal discountValue) {
            this.discountValue = discountValue;
            return this;
        }

        public DiscountApplicationRequestBuilder discountReason(String discountReason) {
            this.discountReason = discountReason;
            return this;
        }

        public DiscountApplicationRequestBuilder schemeCode(String schemeCode) {
            this.schemeCode = schemeCode;
            return this;
        }

        public DiscountApplicationRequestBuilder applicableTo(String applicableTo) {
            this.applicableTo = applicableTo;
            return this;
        }

        public DiscountApplicationRequest build() {
            return new DiscountApplicationRequest(this.invoiceId, this.discountType,
                    this.discountValue, this.discountReason, this.schemeCode, this.applicableTo);
        }
    }
}
