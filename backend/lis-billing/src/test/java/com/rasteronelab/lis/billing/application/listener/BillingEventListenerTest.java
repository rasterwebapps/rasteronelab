package com.rasteronelab.lis.billing.application.listener;

import com.rasteronelab.lis.billing.api.dto.InvoiceResponse;
import com.rasteronelab.lis.billing.application.service.InvoiceService;
import com.rasteronelab.lis.core.event.OrderPlacedEvent;
import com.rasteronelab.lis.core.infrastructure.BranchContextHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("BillingEventListener")
@ExtendWith(MockitoExtension.class)
class BillingEventListenerTest {

    @Mock
    private InvoiceService invoiceService;

    @InjectMocks
    private BillingEventListener billingEventListener;

    @AfterEach
    void tearDown() {
        BranchContextHolder.clear();
    }

    @Test
    @DisplayName("handleOrderPlaced should auto-generate invoice for placed order")
    void handleOrderPlaced_shouldAutoGenerateInvoice() {
        UUID orderId = UUID.randomUUID();
        UUID patientId = UUID.randomUUID();
        UUID branchId = UUID.randomUUID();

        OrderPlacedEvent event = new OrderPlacedEvent(orderId, patientId, branchId);
        InvoiceResponse response = new InvoiceResponse();

        when(invoiceService.generateInvoice(argThat(req ->
                req.getOrderId().equals(orderId) && req.getPatientId().equals(patientId))))
                .thenReturn(response);

        billingEventListener.handleOrderPlaced(event);

        verify(invoiceService).generateInvoice(argThat(req ->
                req.getOrderId().equals(orderId) && req.getPatientId().equals(patientId)));
    }

    @Test
    @DisplayName("handleOrderPlaced should not propagate exceptions")
    void handleOrderPlaced_shouldNotPropagateExceptions() {
        UUID orderId = UUID.randomUUID();
        UUID patientId = UUID.randomUUID();
        UUID branchId = UUID.randomUUID();

        OrderPlacedEvent event = new OrderPlacedEvent(orderId, patientId, branchId);

        when(invoiceService.generateInvoice(argThat(req ->
                req.getOrderId().equals(orderId))))
                .thenThrow(new RuntimeException("DB connection failed"));

        // Should not throw — exception is caught and logged
        billingEventListener.handleOrderPlaced(event);

        verify(invoiceService).generateInvoice(argThat(req ->
                req.getOrderId().equals(orderId)));
    }
}
