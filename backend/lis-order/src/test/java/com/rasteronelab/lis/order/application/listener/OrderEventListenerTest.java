package com.rasteronelab.lis.order.application.listener;

import com.rasteronelab.lis.core.event.PaymentReceivedEvent;
import com.rasteronelab.lis.core.event.SampleCollectedEvent;
import com.rasteronelab.lis.core.infrastructure.BranchContextHolder;
import com.rasteronelab.lis.order.domain.model.OrderStatus;
import com.rasteronelab.lis.order.domain.model.TestOrder;
import com.rasteronelab.lis.order.domain.repository.TestOrderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("OrderEventListener")
@ExtendWith(MockitoExtension.class)
class OrderEventListenerTest {

    @Mock
    private TestOrderRepository testOrderRepository;

    @InjectMocks
    private OrderEventListener orderEventListener;

    @AfterEach
    void tearDown() {
        BranchContextHolder.clear();
    }

    @Test
    @DisplayName("handlePaymentReceived should transition PLACED to PAID when fully paid")
    void handlePaymentReceived_fullyPaid_shouldTransitionToPaid() {
        UUID orderId = UUID.randomUUID();
        UUID branchId = UUID.randomUUID();
        TestOrder order = new TestOrder();
        order.setStatus(OrderStatus.PLACED);

        PaymentReceivedEvent event = new PaymentReceivedEvent(
                UUID.randomUUID(), orderId, branchId, new BigDecimal("500.00"), true);

        when(testOrderRepository.findByIdAndBranchIdAndIsDeletedFalse(orderId, branchId))
                .thenReturn(Optional.of(order));

        orderEventListener.handlePaymentReceived(event);

        assertThat(order.getStatus()).isEqualTo(OrderStatus.PAID);
        verify(testOrderRepository).save(order);
    }

    @Test
    @DisplayName("handlePaymentReceived should not transition on partial payment")
    void handlePaymentReceived_partialPayment_shouldNotTransition() {
        UUID orderId = UUID.randomUUID();
        UUID branchId = UUID.randomUUID();

        PaymentReceivedEvent event = new PaymentReceivedEvent(
                UUID.randomUUID(), orderId, branchId, new BigDecimal("100.00"), false);

        orderEventListener.handlePaymentReceived(event);

        verify(testOrderRepository, never()).findByIdAndBranchIdAndIsDeletedFalse(orderId, branchId);
    }

    @Test
    @DisplayName("handlePaymentReceived should not transition order that is not PLACED")
    void handlePaymentReceived_notPlacedOrder_shouldNotTransition() {
        UUID orderId = UUID.randomUUID();
        UUID branchId = UUID.randomUUID();
        TestOrder order = new TestOrder();
        order.setStatus(OrderStatus.PAID);

        PaymentReceivedEvent event = new PaymentReceivedEvent(
                UUID.randomUUID(), orderId, branchId, new BigDecimal("500.00"), true);

        when(testOrderRepository.findByIdAndBranchIdAndIsDeletedFalse(orderId, branchId))
                .thenReturn(Optional.of(order));

        orderEventListener.handlePaymentReceived(event);

        assertThat(order.getStatus()).isEqualTo(OrderStatus.PAID);
        verify(testOrderRepository, never()).save(order);
    }

    @Test
    @DisplayName("handleSampleCollected should transition PAID to SAMPLE_COLLECTED")
    void handleSampleCollected_shouldTransitionToSampleCollected() {
        UUID orderId = UUID.randomUUID();
        UUID branchId = UUID.randomUUID();
        TestOrder order = new TestOrder();
        order.setStatus(OrderStatus.PAID);

        SampleCollectedEvent event = new SampleCollectedEvent(
                UUID.randomUUID(), orderId, UUID.randomUUID(), branchId);

        when(testOrderRepository.findByIdAndBranchIdAndIsDeletedFalse(orderId, branchId))
                .thenReturn(Optional.of(order));

        orderEventListener.handleSampleCollected(event);

        assertThat(order.getStatus()).isEqualTo(OrderStatus.SAMPLE_COLLECTED);
        verify(testOrderRepository).save(order);
    }

    @Test
    @DisplayName("handleSampleCollected should not transition order that is not PAID")
    void handleSampleCollected_notPaidOrder_shouldNotTransition() {
        UUID orderId = UUID.randomUUID();
        UUID branchId = UUID.randomUUID();
        TestOrder order = new TestOrder();
        order.setStatus(OrderStatus.PLACED);

        SampleCollectedEvent event = new SampleCollectedEvent(
                UUID.randomUUID(), orderId, UUID.randomUUID(), branchId);

        when(testOrderRepository.findByIdAndBranchIdAndIsDeletedFalse(orderId, branchId))
                .thenReturn(Optional.of(order));

        orderEventListener.handleSampleCollected(event);

        assertThat(order.getStatus()).isEqualTo(OrderStatus.PLACED);
        verify(testOrderRepository, never()).save(order);
    }
}
