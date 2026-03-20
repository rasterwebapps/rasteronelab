package com.rasteronelab.lis.sample.api.dto;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for a single sample timeline event.
 */
public class SampleTrackingResponse {

    private UUID id;
    private UUID sampleId;
    private String status;
    private LocalDateTime eventTime;
    private UUID performedBy;
    private String comments;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getSampleId() { return sampleId; }
    public void setSampleId(UUID sampleId) { this.sampleId = sampleId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getEventTime() { return eventTime; }
    public void setEventTime(LocalDateTime eventTime) { this.eventTime = eventTime; }

    public UUID getPerformedBy() { return performedBy; }
    public void setPerformedBy(UUID performedBy) { this.performedBy = performedBy; }

    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
}
