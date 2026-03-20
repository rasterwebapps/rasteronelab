package com.rasteronelab.lis.core.event;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Published when a sample is received at lab reception (status → RECEIVED).
 * Triggers routing to department worklist for processing.
 */
public class SampleReceivedEvent {

    private final UUID sampleId;
    private final UUID orderId;
    private final UUID patientId;
    private final UUID branchId;
    private final LocalDateTime eventTime;

    public SampleReceivedEvent(UUID sampleId, UUID orderId, UUID patientId, UUID branchId) {
        this.sampleId = sampleId;
        this.orderId = orderId;
        this.patientId = patientId;
        this.branchId = branchId;
        this.eventTime = LocalDateTime.now();
    }

    public UUID getSampleId() { return sampleId; }
    public UUID getOrderId() { return orderId; }
    public UUID getPatientId() { return patientId; }
    public UUID getBranchId() { return branchId; }
    public LocalDateTime getEventTime() { return eventTime; }
}
