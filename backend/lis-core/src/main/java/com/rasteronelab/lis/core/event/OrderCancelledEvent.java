package com.rasteronelab.lis.core.event;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Published when an order is cancelled.
 * Billing module listens to void or credit the associated invoice.
 */
public class OrderCancelledEvent {
    private final UUID orderId;
    private final UUID branchId;
    private final String cancelReason;
    private final LocalDateTime eventTime;

    public OrderCancelledEvent(UUID orderId, UUID branchId, String cancelReason) {
        this.orderId = orderId;
        this.branchId = branchId;
        this.cancelReason = cancelReason;
        this.eventTime = LocalDateTime.now();
    }

    public UUID getOrderId() { return orderId; }
    public UUID getBranchId() { return branchId; }
    public String getCancelReason() { return cancelReason; }
    public LocalDateTime getEventTime() { return eventTime; }
}
