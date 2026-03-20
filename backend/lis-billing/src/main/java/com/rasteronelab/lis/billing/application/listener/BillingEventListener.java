package com.rasteronelab.lis.billing.application.listener;

import com.rasteronelab.lis.billing.api.dto.InvoiceRequest;
import com.rasteronelab.lis.billing.application.service.InvoiceService;
import com.rasteronelab.lis.core.event.OrderPlacedEvent;
import com.rasteronelab.lis.core.infrastructure.BranchContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Listens for cross-module events and triggers billing operations.
 * - OrderPlacedEvent → auto-generates a draft invoice for the order.
 */
@Component
public class BillingEventListener {

    private static final Logger log = LoggerFactory.getLogger(BillingEventListener.class);

    private final InvoiceService invoiceService;

    public BillingEventListener(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @EventListener
    @Transactional
    public void handleOrderPlaced(OrderPlacedEvent event) {
        BranchContextHolder.setCurrentBranchId(event.getBranchId());
        try {
            InvoiceRequest request = InvoiceRequest.builder()
                    .orderId(event.getOrderId())
                    .patientId(event.getPatientId())
                    .build();

            invoiceService.generateInvoice(request);
            log.info("Auto-generated invoice for order {} in branch {}",
                    event.getOrderId(), event.getBranchId());
        } catch (Exception e) {
            log.error("Failed to auto-generate invoice for order {}: {}",
                    event.getOrderId(), e.getMessage(), e);
        } finally {
            BranchContextHolder.clear();
        }
    }
}
