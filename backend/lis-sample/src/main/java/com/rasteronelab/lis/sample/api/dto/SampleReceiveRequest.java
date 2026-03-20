package com.rasteronelab.lis.sample.api.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Request to receive (accept) a sample at lab reception.
 */
public class SampleReceiveRequest {

    @NotNull
    private UUID receivedBy;

    @NotNull
    private LocalDateTime receivedAt;

    private BigDecimal temperature;

    private String notes;

    public UUID getReceivedBy() { return receivedBy; }
    public void setReceivedBy(UUID receivedBy) { this.receivedBy = receivedBy; }

    public LocalDateTime getReceivedAt() { return receivedAt; }
    public void setReceivedAt(LocalDateTime receivedAt) { this.receivedAt = receivedAt; }

    public BigDecimal getTemperature() { return temperature; }
    public void setTemperature(BigDecimal temperature) { this.temperature = temperature; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
