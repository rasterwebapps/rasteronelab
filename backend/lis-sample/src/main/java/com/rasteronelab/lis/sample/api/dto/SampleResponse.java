package com.rasteronelab.lis.sample.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for a Sample.
 */
public class SampleResponse {

    private UUID id;
    private UUID orderId;
    private UUID patientId;
    private String sampleBarcode;
    private String tubeType;
    private String sampleType;
    private String status;
    private UUID collectedBy;
    private LocalDateTime collectedAt;
    private String collectionSite;
    private BigDecimal quantity;
    private String unit;
    private Boolean homeCollection;
    private String notes;

    // Receiving fields
    private UUID receivedBy;
    private LocalDateTime receivedAt;
    private LocalDateTime tatStartedAt;

    // Rejection fields
    private String rejectionReason;
    private String rejectionComment;
    private Boolean recollectionRequired;
    private UUID rejectedBy;
    private LocalDateTime rejectedAt;

    // Aliquot fields
    private UUID parentSampleId;
    private String aliquotLabel;

    // Storage fields
    private String storageRack;
    private String storageShelf;
    private String storagePosition;
    private LocalDateTime storedAt;
    private LocalDateTime disposalDate;

    private UUID departmentId;
    private UUID branchId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getOrderId() { return orderId; }
    public void setOrderId(UUID orderId) { this.orderId = orderId; }

    public UUID getPatientId() { return patientId; }
    public void setPatientId(UUID patientId) { this.patientId = patientId; }

    public String getSampleBarcode() { return sampleBarcode; }
    public void setSampleBarcode(String sampleBarcode) { this.sampleBarcode = sampleBarcode; }

    public String getTubeType() { return tubeType; }
    public void setTubeType(String tubeType) { this.tubeType = tubeType; }

    public String getSampleType() { return sampleType; }
    public void setSampleType(String sampleType) { this.sampleType = sampleType; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public UUID getCollectedBy() { return collectedBy; }
    public void setCollectedBy(UUID collectedBy) { this.collectedBy = collectedBy; }

    public LocalDateTime getCollectedAt() { return collectedAt; }
    public void setCollectedAt(LocalDateTime collectedAt) { this.collectedAt = collectedAt; }

    public String getCollectionSite() { return collectionSite; }
    public void setCollectionSite(String collectionSite) { this.collectionSite = collectionSite; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public Boolean getHomeCollection() { return homeCollection; }
    public void setHomeCollection(Boolean homeCollection) { this.homeCollection = homeCollection; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public UUID getReceivedBy() { return receivedBy; }
    public void setReceivedBy(UUID receivedBy) { this.receivedBy = receivedBy; }

    public LocalDateTime getReceivedAt() { return receivedAt; }
    public void setReceivedAt(LocalDateTime receivedAt) { this.receivedAt = receivedAt; }

    public LocalDateTime getTatStartedAt() { return tatStartedAt; }
    public void setTatStartedAt(LocalDateTime tatStartedAt) { this.tatStartedAt = tatStartedAt; }

    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }

    public String getRejectionComment() { return rejectionComment; }
    public void setRejectionComment(String rejectionComment) { this.rejectionComment = rejectionComment; }

    public Boolean getRecollectionRequired() { return recollectionRequired; }
    public void setRecollectionRequired(Boolean recollectionRequired) { this.recollectionRequired = recollectionRequired; }

    public UUID getRejectedBy() { return rejectedBy; }
    public void setRejectedBy(UUID rejectedBy) { this.rejectedBy = rejectedBy; }

    public LocalDateTime getRejectedAt() { return rejectedAt; }
    public void setRejectedAt(LocalDateTime rejectedAt) { this.rejectedAt = rejectedAt; }

    public UUID getParentSampleId() { return parentSampleId; }
    public void setParentSampleId(UUID parentSampleId) { this.parentSampleId = parentSampleId; }

    public String getAliquotLabel() { return aliquotLabel; }
    public void setAliquotLabel(String aliquotLabel) { this.aliquotLabel = aliquotLabel; }

    public String getStorageRack() { return storageRack; }
    public void setStorageRack(String storageRack) { this.storageRack = storageRack; }

    public String getStorageShelf() { return storageShelf; }
    public void setStorageShelf(String storageShelf) { this.storageShelf = storageShelf; }

    public String getStoragePosition() { return storagePosition; }
    public void setStoragePosition(String storagePosition) { this.storagePosition = storagePosition; }

    public LocalDateTime getStoredAt() { return storedAt; }
    public void setStoredAt(LocalDateTime storedAt) { this.storedAt = storedAt; }

    public LocalDateTime getDisposalDate() { return disposalDate; }
    public void setDisposalDate(LocalDateTime disposalDate) { this.disposalDate = disposalDate; }

    public UUID getDepartmentId() { return departmentId; }
    public void setDepartmentId(UUID departmentId) { this.departmentId = departmentId; }

    public UUID getBranchId() { return branchId; }
    public void setBranchId(UUID branchId) { this.branchId = branchId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
}
