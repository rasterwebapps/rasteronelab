package com.rasteronelab.lis.sample.domain.model;

import com.rasteronelab.lis.core.domain.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Sample entity — represents one physical tube collected for a test order.
 * One order may produce multiple samples (one per tube type).
 * Extends BaseEntity for multi-branch support via branchId.
 */
@Entity
@Table(name = "sample")
public class Sample extends BaseEntity {

    @NotNull
    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @NotNull
    @Column(name = "patient_id", nullable = false)
    private UUID patientId;

    @Column(name = "sample_barcode", nullable = false, unique = true)
    private String sampleBarcode;

    @Enumerated(EnumType.STRING)
    @Column(name = "tube_type", nullable = false)
    private TubeType tubeType;

    @Column(name = "sample_type")
    private String sampleType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SampleStatus status = SampleStatus.COLLECTED;

    @Column(name = "collected_by", nullable = false)
    private UUID collectedBy;

    @Column(name = "collected_at", nullable = false)
    private LocalDateTime collectedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "collection_site")
    private CollectionSite collectionSite;

    @Column(name = "quantity")
    private BigDecimal quantity;

    @Column(name = "unit")
    private String unit;

    @Column(name = "home_collection")
    private Boolean homeCollection = false;

    @Column(name = "notes")
    private String notes;

    // --- Receiving fields ---

    @Column(name = "received_by")
    private UUID receivedBy;

    @Column(name = "received_at")
    private LocalDateTime receivedAt;

    @Column(name = "tat_started_at")
    private LocalDateTime tatStartedAt;

    @Column(name = "temperature")
    private BigDecimal temperature;

    // --- Rejection fields ---

    @Enumerated(EnumType.STRING)
    @Column(name = "rejection_reason")
    private RejectionReason rejectionReason;

    @Column(name = "rejection_comment")
    private String rejectionComment;

    @Column(name = "recollection_required")
    private Boolean recollectionRequired = false;

    @Column(name = "rejected_by")
    private UUID rejectedBy;

    @Column(name = "rejected_at")
    private LocalDateTime rejectedAt;

    // --- Aliquot fields ---

    @Column(name = "parent_sample_id")
    private UUID parentSampleId;

    @Column(name = "aliquot_label")
    private String aliquotLabel;

    @Column(name = "aliquot_sequence")
    private Integer aliquotSequence;

    // --- Storage fields ---

    @Column(name = "storage_rack")
    private String storageRack;

    @Column(name = "storage_shelf")
    private String storageShelf;

    @Column(name = "storage_position")
    private String storagePosition;

    @Column(name = "stored_at")
    private LocalDateTime storedAt;

    @Column(name = "disposal_date")
    private LocalDateTime disposalDate;

    @Column(name = "department_id")
    private UUID departmentId;

    // --- Getters and Setters ---

    public UUID getOrderId() { return orderId; }
    public void setOrderId(UUID orderId) { this.orderId = orderId; }

    public UUID getPatientId() { return patientId; }
    public void setPatientId(UUID patientId) { this.patientId = patientId; }

    public String getSampleBarcode() { return sampleBarcode; }
    public void setSampleBarcode(String sampleBarcode) { this.sampleBarcode = sampleBarcode; }

    public TubeType getTubeType() { return tubeType; }
    public void setTubeType(TubeType tubeType) { this.tubeType = tubeType; }

    public String getSampleType() { return sampleType; }
    public void setSampleType(String sampleType) { this.sampleType = sampleType; }

    public SampleStatus getStatus() { return status; }
    public void setStatus(SampleStatus status) { this.status = status; }

    public UUID getCollectedBy() { return collectedBy; }
    public void setCollectedBy(UUID collectedBy) { this.collectedBy = collectedBy; }

    public LocalDateTime getCollectedAt() { return collectedAt; }
    public void setCollectedAt(LocalDateTime collectedAt) { this.collectedAt = collectedAt; }

    public CollectionSite getCollectionSite() { return collectionSite; }
    public void setCollectionSite(CollectionSite collectionSite) { this.collectionSite = collectionSite; }

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

    public BigDecimal getTemperature() { return temperature; }
    public void setTemperature(BigDecimal temperature) { this.temperature = temperature; }

    public RejectionReason getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(RejectionReason rejectionReason) { this.rejectionReason = rejectionReason; }

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

    public Integer getAliquotSequence() { return aliquotSequence; }
    public void setAliquotSequence(Integer aliquotSequence) { this.aliquotSequence = aliquotSequence; }

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
}
