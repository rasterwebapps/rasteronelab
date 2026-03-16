package com.rasteronelab.lis.core.common.enums;

/**
 * Invoice lifecycle states.
 */
public enum InvoiceStatus {
    DRAFT,
    GENERATED,
    SENT,
    PARTIALLY_PAID,
    PAID,
    OVERDUE,
    CANCELLED,
    REFUNDED,
    WRITTEN_OFF
}
