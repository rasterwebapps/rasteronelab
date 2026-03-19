package com.rasteronelab.lis.core.event;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Published when an order transitions from DRAFT to PLACED.
 * Billing module listens to auto-generate invoice.
 */
public class OrderPlacedEvent {
    private final UUID orderId;
    private final UUID patientId;
    private final UUID branchId;
    private final LocalDateTime eventTime;

    public OrderPlacedEvent(UUID orderId, UUID patientId, UUID branchId) {
        this.orderId = orderId;
        this.patientId = patientId;
        this.branchId = branchId;
        this.eventTime = LocalDateTime.now();
    }

    public UUID getOrderId() { return orderId; }
    public UUID getPatientId() { return patientId; }
    public UUID getBranchId() { return branchId; }
    public LocalDateTime getEventTime() { return eventTime; }
}
