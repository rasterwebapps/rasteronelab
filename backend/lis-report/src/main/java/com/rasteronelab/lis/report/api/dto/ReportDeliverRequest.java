package com.rasteronelab.lis.report.api.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO for delivering a lab report.
 */
public class ReportDeliverRequest {

    @NotBlank
    private String deliveryChannel;

    private String recipientEmail;

    private String recipientPhone;

    public ReportDeliverRequest() {
    }

    private ReportDeliverRequest(Builder builder) {
        this.deliveryChannel = builder.deliveryChannel;
        this.recipientEmail = builder.recipientEmail;
        this.recipientPhone = builder.recipientPhone;
    }

    public String getDeliveryChannel() { return deliveryChannel; }
    public void setDeliveryChannel(String deliveryChannel) { this.deliveryChannel = deliveryChannel; }

    public String getRecipientEmail() { return recipientEmail; }
    public void setRecipientEmail(String recipientEmail) { this.recipientEmail = recipientEmail; }

    public String getRecipientPhone() { return recipientPhone; }
    public void setRecipientPhone(String recipientPhone) { this.recipientPhone = recipientPhone; }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String deliveryChannel;
        private String recipientEmail;
        private String recipientPhone;

        public Builder deliveryChannel(String deliveryChannel) { this.deliveryChannel = deliveryChannel; return this; }
        public Builder recipientEmail(String recipientEmail) { this.recipientEmail = recipientEmail; return this; }
        public Builder recipientPhone(String recipientPhone) { this.recipientPhone = recipientPhone; return this; }

        public ReportDeliverRequest build() {
            return new ReportDeliverRequest(this);
        }
    }
}
