package com.rasteronelab.lis.core.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Published when a payment is recorded against an invoice.
 * Order module listens to update order status to PAID when fully paid.
 */
public class PaymentReceivedEvent {
    private final UUID invoiceId;
    private final UUID orderId;
    private final UUID branchId;
    private final BigDecimal amount;
    private final boolean fullyPaid;
    private final LocalDateTime eventTime;

    public PaymentReceivedEvent(UUID invoiceId, UUID orderId, UUID branchId, BigDecimal amount, boolean fullyPaid) {
        this.invoiceId = invoiceId;
        this.orderId = orderId;
        this.branchId = branchId;
        this.amount = amount;
        this.fullyPaid = fullyPaid;
        this.eventTime = LocalDateTime.now();
    }

    public UUID getInvoiceId() { return invoiceId; }
    public UUID getOrderId() { return orderId; }
    public UUID getBranchId() { return branchId; }
    public BigDecimal getAmount() { return amount; }
    public boolean isFullyPaid() { return fullyPaid; }
    public LocalDateTime getEventTime() { return eventTime; }
}
