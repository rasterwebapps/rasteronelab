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
 * Audit trail for every sample state transition.
 * One row per event in the sample lifecycle timeline.
 */
@Entity
@Table(name = "sample_tracking")
public class SampleTracking extends BaseEntity {

    @NotNull
    @Column(name = "sample_id", nullable = false)
    private UUID sampleId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SampleStatus status;

    @Column(name = "event_time", nullable = false)
    private LocalDateTime eventTime;

    @Column(name = "performed_by")
    private UUID performedBy;

    @Column(name = "comments")
    private String comments;

    // Getters & Setters

    public UUID getSampleId() { return sampleId; }
    public void setSampleId(UUID sampleId) { this.sampleId = sampleId; }

    public SampleStatus getStatus() { return status; }
    public void setStatus(SampleStatus status) { this.status = status; }

    public LocalDateTime getEventTime() { return eventTime; }
    public void setEventTime(LocalDateTime eventTime) { this.eventTime = eventTime; }

    public UUID getPerformedBy() { return performedBy; }
    public void setPerformedBy(UUID performedBy) { this.performedBy = performedBy; }

    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
}
