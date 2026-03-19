package com.rasteronelab.lis.billing.domain.model;

/**
 * Invoice lifecycle states within the billing module.
 */
public enum InvoiceStatus {
    DRAFT,
    GENERATED,
    PARTIALLY_PAID,
    PAID,
    REFUNDED,
    CANCELLED
}
