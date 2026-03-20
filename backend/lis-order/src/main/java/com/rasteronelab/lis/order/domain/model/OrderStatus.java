package com.rasteronelab.lis.order.domain.model;

/**
 * Status lifecycle for a TestOrder.
 * Transitions: DRAFT → PLACED → PAID → SAMPLE_COLLECTED → IN_PROGRESS → RESULTED → AUTHORISED → COMPLETED
 * Any non-terminal state → CANCELLED
 */
public enum OrderStatus {
    DRAFT,
    PLACED,
    PAID,
    SAMPLE_COLLECTED,
    IN_PROGRESS,
    RESULTED,
    AUTHORISED,
    COMPLETED,
    CANCELLED
}
