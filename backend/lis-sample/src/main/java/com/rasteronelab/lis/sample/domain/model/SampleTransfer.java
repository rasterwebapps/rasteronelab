package com.rasteronelab.lis.sample.domain.model;

import com.rasteronelab.lis.core.domain.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Records an inter-branch sample transfer request and its lifecycle.
 * Only BRANCH_ADMIN+ may initiate a transfer.
 */
@Entity
@Table(name = "sample_transfer")
public class SampleTransfer extends BaseEntity {

    @NotNull
    @Column(name = "sample_id", nullable = false)
    private UUID sampleId;

    @NotNull
    @Column(name = "source_branch_id", nullable = false)
    private UUID sourceBranchId;

    @NotNull
    @Column(name = "destination_branch_id", nullable = false)
    private UUID destinationBranchId;

    @Column(name = "reason")
    private String reason;

    @Column(name = "transferred_by", nullable = false)
    private UUID transferredBy;

    @Column(name = "transferred_at", nullable = false)
    private LocalDateTime transferredAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TransferStatus status = TransferStatus.INITIATED;

    @Column(name = "received_at")
    private LocalDateTime receivedAt;

    @Column(name = "received_by")
    private UUID receivedBy;

    @Column(name = "notes")
    private String notes;

    // Getters & Setters

    public UUID getSampleId() { return sampleId; }
    public void setSampleId(UUID sampleId) { this.sampleId = sampleId; }

    public UUID getSourceBranchId() { return sourceBranchId; }
    public void setSourceBranchId(UUID sourceBranchId) { this.sourceBranchId = sourceBranchId; }

    public UUID getDestinationBranchId() { return destinationBranchId; }
    public void setDestinationBranchId(UUID destinationBranchId) { this.destinationBranchId = destinationBranchId; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public UUID getTransferredBy() { return transferredBy; }
    public void setTransferredBy(UUID transferredBy) { this.transferredBy = transferredBy; }

    public LocalDateTime getTransferredAt() { return transferredAt; }
    public void setTransferredAt(LocalDateTime transferredAt) { this.transferredAt = transferredAt; }

    public TransferStatus getStatus() { return status; }
    public void setStatus(TransferStatus status) { this.status = status; }

    public LocalDateTime getReceivedAt() { return receivedAt; }
    public void setReceivedAt(LocalDateTime receivedAt) { this.receivedAt = receivedAt; }

    public UUID getReceivedBy() { return receivedBy; }
    public void setReceivedBy(UUID receivedBy) { this.receivedBy = receivedBy; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
