package com.rasteronelab.lis.sample.api.dto;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for a SampleTransfer.
 */
public class SampleTransferResponse {

    private UUID id;
    private UUID sampleId;
    private UUID sourceBranchId;
    private UUID destinationBranchId;
    private String reason;
    private UUID transferredBy;
    private LocalDateTime transferredAt;
    private String status;
    private LocalDateTime receivedAt;
    private UUID receivedBy;
    private String notes;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

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

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getReceivedAt() { return receivedAt; }
    public void setReceivedAt(LocalDateTime receivedAt) { this.receivedAt = receivedAt; }

    public UUID getReceivedBy() { return receivedBy; }
    public void setReceivedBy(UUID receivedBy) { this.receivedBy = receivedBy; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
