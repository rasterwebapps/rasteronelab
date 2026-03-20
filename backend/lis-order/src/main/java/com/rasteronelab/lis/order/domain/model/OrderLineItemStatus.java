package com.rasteronelab.lis.order.domain.model;

/**
 * Status lifecycle for an individual order line item.
 */
public enum OrderLineItemStatus {
    PENDING,
    SAMPLE_COLLECTED,
    IN_PROGRESS,
    RESULTED,
    AUTHORISED,
    COMPLETED,
    CANCELLED
}
