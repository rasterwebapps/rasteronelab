package com.rasteronelab.lis.order.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

/**
 * Request DTO for creating a TestOrder.
 */
public class TestOrderRequest {

    @NotNull(message = "Patient ID is required")
    private UUID patientId;

    private UUID visitId;

    private UUID referringDoctorId;

    private String priority;

    private String clinicalHistory;

    private String specialInstructions;

    @Valid
    private List<OrderLineItemRequest> lineItems;

    public TestOrderRequest() {
    }

    public TestOrderRequest(UUID patientId, UUID visitId, UUID referringDoctorId, String priority, String clinicalHistory, String specialInstructions, List<OrderLineItemRequest> lineItems) {
        this.patientId = patientId;
        this.visitId = visitId;
        this.referringDoctorId = referringDoctorId;
        this.priority = priority;
        this.clinicalHistory = clinicalHistory;
        this.specialInstructions = specialInstructions;
        this.lineItems = lineItems;
    }

    public UUID getPatientId() {
        return this.patientId;
    }

    public UUID getVisitId() {
        return this.visitId;
    }

    public UUID getReferringDoctorId() {
        return this.referringDoctorId;
    }

    public String getPriority() {
        return this.priority;
    }

    public String getClinicalHistory() {
        return this.clinicalHistory;
    }

    public String getSpecialInstructions() {
        return this.specialInstructions;
    }

    public List<OrderLineItemRequest> getLineItems() {
        return this.lineItems;
    }

    public void setPatientId(UUID patientId) {
        this.patientId = patientId;
    }

    public void setVisitId(UUID visitId) {
        this.visitId = visitId;
    }

    public void setReferringDoctorId(UUID referringDoctorId) {
        this.referringDoctorId = referringDoctorId;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setClinicalHistory(String clinicalHistory) {
        this.clinicalHistory = clinicalHistory;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }

    public void setLineItems(List<OrderLineItemRequest> lineItems) {
        this.lineItems = lineItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestOrderRequest that = (TestOrderRequest) o;
        return java.util.Objects.equals(this.patientId, that.patientId) &&
               java.util.Objects.equals(this.visitId, that.visitId) &&
               java.util.Objects.equals(this.referringDoctorId, that.referringDoctorId) &&
               java.util.Objects.equals(this.priority, that.priority) &&
               java.util.Objects.equals(this.clinicalHistory, that.clinicalHistory) &&
               java.util.Objects.equals(this.specialInstructions, that.specialInstructions) &&
               java.util.Objects.equals(this.lineItems, that.lineItems);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.patientId, this.visitId, this.referringDoctorId, this.priority, this.clinicalHistory, this.specialInstructions, this.lineItems);
    }

    @Override
    public String toString() {
        return "TestOrderRequest{patientId=" + patientId +
               ", visitId=" + visitId +
               ", referringDoctorId=" + referringDoctorId +
               ", priority=" + priority +
               ", clinicalHistory=" + clinicalHistory +
               ", specialInstructions=" + specialInstructions +
               ", lineItems=" + lineItems +
               "}";
    }

    public static TestOrderRequestBuilder builder() {
        return new TestOrderRequestBuilder();
    }

    public static class TestOrderRequestBuilder {
        private UUID patientId;
        private UUID visitId;
        private UUID referringDoctorId;
        private String priority;
        private String clinicalHistory;
        private String specialInstructions;
        private List<OrderLineItemRequest> lineItems;

        TestOrderRequestBuilder() {
        }

        public TestOrderRequestBuilder patientId(UUID patientId) {
            this.patientId = patientId;
            return this;
        }

        public TestOrderRequestBuilder visitId(UUID visitId) {
            this.visitId = visitId;
            return this;
        }

        public TestOrderRequestBuilder referringDoctorId(UUID referringDoctorId) {
            this.referringDoctorId = referringDoctorId;
            return this;
        }

        public TestOrderRequestBuilder priority(String priority) {
            this.priority = priority;
            return this;
        }

        public TestOrderRequestBuilder clinicalHistory(String clinicalHistory) {
            this.clinicalHistory = clinicalHistory;
            return this;
        }

        public TestOrderRequestBuilder specialInstructions(String specialInstructions) {
            this.specialInstructions = specialInstructions;
            return this;
        }

        public TestOrderRequestBuilder lineItems(List<OrderLineItemRequest> lineItems) {
            this.lineItems = lineItems;
            return this;
        }

        public TestOrderRequest build() {
            return new TestOrderRequest(this.patientId, this.visitId, this.referringDoctorId, this.priority, this.clinicalHistory, this.specialInstructions, this.lineItems);
        }
    }

}
