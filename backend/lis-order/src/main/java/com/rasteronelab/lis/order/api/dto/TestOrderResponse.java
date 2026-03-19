package com.rasteronelab.lis.order.api.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Response DTO for TestOrder entity.
 * Includes all order fields plus line items and audit metadata.
 */
public class TestOrderResponse {

    private UUID id;
    private UUID branchId;
    private String orderNumber;
    private UUID patientId;
    private UUID visitId;
    private UUID referringDoctorId;
    private String priority;
    private String status;
    private LocalDateTime orderDate;
    private String clinicalHistory;
    private String specialInstructions;
    private String barcode;
    private LocalDateTime estimatedCompletionTime;
    private LocalDateTime completedAt;
    private LocalDateTime cancelledAt;
    private String cancelReason;
    private Boolean isActive;
    private List<OrderLineItemResponse> lineItems;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    public TestOrderResponse() {
    }

    public TestOrderResponse(UUID id, UUID branchId, String orderNumber, UUID patientId, UUID visitId, UUID referringDoctorId, String priority, String status, LocalDateTime orderDate, String clinicalHistory, String specialInstructions, String barcode, LocalDateTime estimatedCompletionTime, LocalDateTime completedAt, LocalDateTime cancelledAt, String cancelReason, Boolean isActive, List<OrderLineItemResponse> lineItems, LocalDateTime createdAt, LocalDateTime updatedAt, String createdBy, String updatedBy) {
        this.id = id;
        this.branchId = branchId;
        this.orderNumber = orderNumber;
        this.patientId = patientId;
        this.visitId = visitId;
        this.referringDoctorId = referringDoctorId;
        this.priority = priority;
        this.status = status;
        this.orderDate = orderDate;
        this.clinicalHistory = clinicalHistory;
        this.specialInstructions = specialInstructions;
        this.barcode = barcode;
        this.estimatedCompletionTime = estimatedCompletionTime;
        this.completedAt = completedAt;
        this.cancelledAt = cancelledAt;
        this.cancelReason = cancelReason;
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

    public UUID getBranchId() {
        return this.branchId;
    }

    public String getOrderNumber() {
        return this.orderNumber;
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

    public String getStatus() {
        return this.status;
    }

    public LocalDateTime getOrderDate() {
        return this.orderDate;
    }

    public String getClinicalHistory() {
        return this.clinicalHistory;
    }

    public String getSpecialInstructions() {
        return this.specialInstructions;
    }

    public String getBarcode() {
        return this.barcode;
    }

    public LocalDateTime getEstimatedCompletionTime() {
        return this.estimatedCompletionTime;
    }

    public LocalDateTime getCompletedAt() {
        return this.completedAt;
    }

    public LocalDateTime getCancelledAt() {
        return this.cancelledAt;
    }

    public String getCancelReason() {
        return this.cancelReason;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public List<OrderLineItemResponse> getLineItems() {
        return this.lineItems;
    }

    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setBranchId(UUID branchId) {
        this.branchId = branchId;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
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

    public void setStatus(String status) {
        this.status = status;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public void setClinicalHistory(String clinicalHistory) {
        this.clinicalHistory = clinicalHistory;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setEstimatedCompletionTime(LocalDateTime estimatedCompletionTime) {
        this.estimatedCompletionTime = estimatedCompletionTime;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public void setCancelledAt(LocalDateTime cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public void setLineItems(List<OrderLineItemResponse> lineItems) {
        this.lineItems = lineItems;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestOrderResponse that = (TestOrderResponse) o;
        return java.util.Objects.equals(this.id, that.id) &&
               java.util.Objects.equals(this.branchId, that.branchId) &&
               java.util.Objects.equals(this.orderNumber, that.orderNumber) &&
               java.util.Objects.equals(this.patientId, that.patientId) &&
               java.util.Objects.equals(this.visitId, that.visitId) &&
               java.util.Objects.equals(this.referringDoctorId, that.referringDoctorId) &&
               java.util.Objects.equals(this.priority, that.priority) &&
               java.util.Objects.equals(this.status, that.status) &&
               java.util.Objects.equals(this.orderDate, that.orderDate) &&
               java.util.Objects.equals(this.clinicalHistory, that.clinicalHistory) &&
               java.util.Objects.equals(this.specialInstructions, that.specialInstructions) &&
               java.util.Objects.equals(this.barcode, that.barcode) &&
               java.util.Objects.equals(this.estimatedCompletionTime, that.estimatedCompletionTime) &&
               java.util.Objects.equals(this.completedAt, that.completedAt) &&
               java.util.Objects.equals(this.cancelledAt, that.cancelledAt) &&
               java.util.Objects.equals(this.cancelReason, that.cancelReason) &&
               java.util.Objects.equals(this.isActive, that.isActive) &&
               java.util.Objects.equals(this.lineItems, that.lineItems) &&
               java.util.Objects.equals(this.createdAt, that.createdAt) &&
               java.util.Objects.equals(this.updatedAt, that.updatedAt) &&
               java.util.Objects.equals(this.createdBy, that.createdBy) &&
               java.util.Objects.equals(this.updatedBy, that.updatedBy);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.id, this.branchId, this.orderNumber, this.patientId, this.visitId, this.referringDoctorId, this.priority, this.status, this.orderDate, this.clinicalHistory, this.specialInstructions, this.barcode, this.estimatedCompletionTime, this.completedAt, this.cancelledAt, this.cancelReason, this.isActive, this.lineItems, this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
    }

    @Override
    public String toString() {
        return "TestOrderResponse{id=" + id +
               ", branchId=" + branchId +
               ", orderNumber=" + orderNumber +
               ", patientId=" + patientId +
               ", visitId=" + visitId +
               ", referringDoctorId=" + referringDoctorId +
               ", priority=" + priority +
               ", status=" + status +
               ", orderDate=" + orderDate +
               ", clinicalHistory=" + clinicalHistory +
               ", specialInstructions=" + specialInstructions +
               ", barcode=" + barcode +
               ", estimatedCompletionTime=" + estimatedCompletionTime +
               ", completedAt=" + completedAt +
               ", cancelledAt=" + cancelledAt +
               ", cancelReason=" + cancelReason +
               ", isActive=" + isActive +
               ", lineItems=" + lineItems +
               ", createdAt=" + createdAt +
               ", updatedAt=" + updatedAt +
               ", createdBy=" + createdBy +
               ", updatedBy=" + updatedBy +
               "}";
    }

    public static TestOrderResponseBuilder builder() {
        return new TestOrderResponseBuilder();
    }

    public static class TestOrderResponseBuilder {
        private UUID id;
        private UUID branchId;
        private String orderNumber;
        private UUID patientId;
        private UUID visitId;
        private UUID referringDoctorId;
        private String priority;
        private String status;
        private LocalDateTime orderDate;
        private String clinicalHistory;
        private String specialInstructions;
        private String barcode;
        private LocalDateTime estimatedCompletionTime;
        private LocalDateTime completedAt;
        private LocalDateTime cancelledAt;
        private String cancelReason;
        private Boolean isActive;
        private List<OrderLineItemResponse> lineItems;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private String createdBy;
        private String updatedBy;

        TestOrderResponseBuilder() {
        }

        public TestOrderResponseBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public TestOrderResponseBuilder branchId(UUID branchId) {
            this.branchId = branchId;
            return this;
        }

        public TestOrderResponseBuilder orderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
            return this;
        }

        public TestOrderResponseBuilder patientId(UUID patientId) {
            this.patientId = patientId;
            return this;
        }

        public TestOrderResponseBuilder visitId(UUID visitId) {
            this.visitId = visitId;
            return this;
        }

        public TestOrderResponseBuilder referringDoctorId(UUID referringDoctorId) {
            this.referringDoctorId = referringDoctorId;
            return this;
        }

        public TestOrderResponseBuilder priority(String priority) {
            this.priority = priority;
            return this;
        }

        public TestOrderResponseBuilder status(String status) {
            this.status = status;
            return this;
        }

        public TestOrderResponseBuilder orderDate(LocalDateTime orderDate) {
            this.orderDate = orderDate;
            return this;
        }

        public TestOrderResponseBuilder clinicalHistory(String clinicalHistory) {
            this.clinicalHistory = clinicalHistory;
            return this;
        }

        public TestOrderResponseBuilder specialInstructions(String specialInstructions) {
            this.specialInstructions = specialInstructions;
            return this;
        }

        public TestOrderResponseBuilder barcode(String barcode) {
            this.barcode = barcode;
            return this;
        }

        public TestOrderResponseBuilder estimatedCompletionTime(LocalDateTime estimatedCompletionTime) {
            this.estimatedCompletionTime = estimatedCompletionTime;
            return this;
        }

        public TestOrderResponseBuilder completedAt(LocalDateTime completedAt) {
            this.completedAt = completedAt;
            return this;
        }

        public TestOrderResponseBuilder cancelledAt(LocalDateTime cancelledAt) {
            this.cancelledAt = cancelledAt;
            return this;
        }

        public TestOrderResponseBuilder cancelReason(String cancelReason) {
            this.cancelReason = cancelReason;
            return this;
        }

        public TestOrderResponseBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public TestOrderResponseBuilder lineItems(List<OrderLineItemResponse> lineItems) {
            this.lineItems = lineItems;
            return this;
        }

        public TestOrderResponseBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public TestOrderResponseBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public TestOrderResponseBuilder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public TestOrderResponseBuilder updatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
            return this;
        }

        public TestOrderResponse build() {
            return new TestOrderResponse(this.id, this.branchId, this.orderNumber, this.patientId, this.visitId, this.referringDoctorId, this.priority, this.status, this.orderDate, this.clinicalHistory, this.specialInstructions, this.barcode, this.estimatedCompletionTime, this.completedAt, this.cancelledAt, this.cancelReason, this.isActive, this.lineItems, this.createdAt, this.updatedAt, this.createdBy, this.updatedBy);
        }
    }

}
