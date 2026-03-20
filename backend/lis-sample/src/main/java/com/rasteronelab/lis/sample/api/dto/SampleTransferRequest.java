package com.rasteronelab.lis.sample.api.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Request to initiate an inter-branch sample transfer.
 */
public class SampleTransferRequest {

    @NotNull
    private UUID destinationBranchId;

    @NotNull
    private UUID transferredBy;

    private String reason;

    private String notes;

    public UUID getDestinationBranchId() { return destinationBranchId; }
    public void setDestinationBranchId(UUID destinationBranchId) { this.destinationBranchId = destinationBranchId; }

    public UUID getTransferredBy() { return transferredBy; }
    public void setTransferredBy(UUID transferredBy) { this.transferredBy = transferredBy; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
