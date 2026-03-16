package com.rasteronelab.lis.core.common.enums;

/**
 * Lifecycle states for a test order.
 * Transitions: DRAFT → PLACED → PAID → SAMPLE_COLLECTED → SAMPLE_RECEIVED
 *              → PROCESSING → RESULT_PENDING → RESULT_AVAILABLE
 *              → AUTHORIZED → REPORT_GENERATED → DELIVERED → COMPLETED
 * Cancel is possible from DRAFT, PLACED, PAID states only.
 */
public enum OrderStatus {
    DRAFT,
    PLACED,
    PAID,
    SAMPLE_COLLECTED,
    SAMPLE_RECEIVED,
    PROCESSING,
    RESULT_PENDING,
    RESULT_AVAILABLE,
    AUTHORIZED,
    REPORT_GENERATED,
    DELIVERED,
    COMPLETED,
    CANCELLED,
    ON_HOLD
}
