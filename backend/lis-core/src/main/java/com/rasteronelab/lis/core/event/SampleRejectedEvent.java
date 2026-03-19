package com.rasteronelab.lis.core.event;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Published when a sample is rejected at lab reception.
 * Notification module listens to trigger recollection alert.
 */
public class SampleRejectedEvent {

    private final UUID sampleId;
    private final UUID orderId;
    private final UUID patientId;
    private final UUID branchId;
    private final String rejectionReason;
    private final boolean recollectionRequired;
    private final LocalDateTime eventTime;

    public SampleRejectedEvent(UUID sampleId, UUID orderId, UUID patientId, UUID branchId,
                               String rejectionReason, boolean recollectionRequired) {
        this.sampleId = sampleId;
        this.orderId = orderId;
        this.patientId = patientId;
        this.branchId = branchId;
        this.rejectionReason = rejectionReason;
        this.recollectionRequired = recollectionRequired;
        this.eventTime = LocalDateTime.now();
    }

    public UUID getSampleId() { return sampleId; }
    public UUID getOrderId() { return orderId; }
    public UUID getPatientId() { return patientId; }
    public UUID getBranchId() { return branchId; }
    public String getRejectionReason() { return rejectionReason; }
    public boolean isRecollectionRequired() { return recollectionRequired; }
    public LocalDateTime getEventTime() { return eventTime; }
}
