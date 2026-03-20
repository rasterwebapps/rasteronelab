package com.rasteronelab.lis.billing.domain.model;

/**
 * Refund processing lifecycle states.
 */
public enum RefundStatus {
    REQUESTED,
    APPROVED,
    PROCESSED,
    REJECTED
}
