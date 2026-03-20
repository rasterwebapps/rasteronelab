package com.rasteronelab.lis.order.application.service;

import com.rasteronelab.lis.core.common.exception.BusinessRuleException;
import com.rasteronelab.lis.core.event.OrderCancelledEvent;
import com.rasteronelab.lis.core.event.OrderPlacedEvent;
import com.rasteronelab.lis.core.event.PaymentReceivedEvent;
import com.rasteronelab.lis.core.event.SampleCollectedEvent;
import com.rasteronelab.lis.core.infrastructure.BranchContextHolder;
import com.rasteronelab.lis.order.api.dto.OrderLineItemRequest;
import com.rasteronelab.lis.order.api.dto.OrderValidationResponse;
import com.rasteronelab.lis.order.api.dto.SampleGroupResponse;
import com.rasteronelab.lis.order.api.dto.TestOrderRequest;
import com.rasteronelab.lis.order.api.dto.TestOrderResponse;
import com.rasteronelab.lis.order.api.mapper.OrderLineItemMapper;
import com.rasteronelab.lis.order.api.mapper.TestOrderMapper;
import com.rasteronelab.lis.order.application.listener.OrderEventListener;
import com.rasteronelab.lis.order.domain.model.OrderLineItem;
import com.rasteronelab.lis.order.domain.model.OrderLineItemStatus;
import com.rasteronelab.lis.order.domain.model.OrderStatus;
import com.rasteronelab.lis.order.domain.model.Priority;
import com.rasteronelab.lis.order.domain.model.TestOrder;
import com.rasteronelab.lis.order.domain.repository.TestOrderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Integration flow tests for the order lifecycle.
 * Verifies complete order state machine transitions and cross-module
 * event-driven interactions using Spring ApplicationEvents.
 */
@DisplayName("OrderLifecycleFlowTest - Cross-module event-driven integration flow")
@ExtendWith(MockitoExtension.class)
class OrderLifecycleFlowTest {

    private static final UUID BRANCH_ID = UUID.randomUUID();

    @Mock
    private TestOrderRepository testOrderRepository;

    @Mock
    private TestOrderMapper testOrderMapper;

    @Mock
    private OrderLineItemMapper orderLineItemMapper;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private TestOrderService testOrderService;

    private OrderEventListener orderEventListener;

    @BeforeEach
    void setUp() {
        BranchContextHolder.setCurrentBranchId(BRANCH_ID);
        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken("lab-tech", null, "ROLE_LAB_TECHNICIAN"));
        orderEventListener = new OrderEventListener(testOrderRepository);
    }

    @AfterEach
    void tearDown() {
        BranchContextHolder.clear();
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Full order lifecycle: DRAFT → PLACED → PAID → SAMPLE_COLLECTED → IN_PROGRESS → RESULTED → AUTHORISED → COMPLETED")
    void fullOrderLifecycle_draftToCompleted() {
        UUID orderId = UUID.randomUUID();
        UUID patientId = UUID.randomUUID();

        TestOrder order = new TestOrder();
        order.setId(orderId);
        order.setPatientId(patientId);

        OrderLineItemRequest itemRequest = new OrderLineItemRequest();
        itemRequest.setTestId(UUID.randomUUID());
        itemRequest.setTestCode("CBC");
        itemRequest.setTestName("Complete Blood Count");
        itemRequest.setUnitPrice(new BigDecimal("500.00"));
        itemRequest.setDiscountAmount(new BigDecimal("50.00"));

        TestOrderRequest request = new TestOrderRequest();
        request.setPatientId(patientId);
        request.setPriority("ROUTINE");
        request.setLineItems(Collections.singletonList(itemRequest));

        OrderLineItem lineItem = new OrderLineItem();
        lineItem.setUnitPrice(new BigDecimal("500.00"));
        lineItem.setDiscountAmount(new BigDecimal("50.00"));
        lineItem.setIsActive(true);

        TestOrderResponse response = new TestOrderResponse();

        when(testOrderMapper.toEntity(request)).thenReturn(order);
        when(orderLineItemMapper.toEntity(itemRequest)).thenReturn(lineItem);
        when(testOrderRepository.countByBranchIdAndIsDeletedFalseAndOrderDateBetween(
                eq(BRANCH_ID), any(), any())).thenReturn(0L);
        when(testOrderRepository.save(order)).thenReturn(order);
        when(testOrderMapper.toResponse(order)).thenReturn(response);
        when(testOrderRepository.findByIdAndBranchIdAndIsDeletedFalse(orderId, BRANCH_ID))
                .thenReturn(Optional.of(order));

        // Step 1: CREATE → DRAFT
        testOrderService.create(request);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.DRAFT);
        assertThat(order.getOrderNumber()).startsWith("ORD-");
        assertThat(order.getBarcode()).isNotNull();
        assertThat(order.getPriority()).isEqualTo(Priority.ROUTINE);
        assertThat(order.getBranchId()).isEqualTo(BRANCH_ID);

        // Step 2: DRAFT → PLACED
        testOrderService.placeOrder(orderId);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PLACED);

        // Verify OrderPlacedEvent published with correct fields
        ArgumentCaptor<OrderPlacedEvent> placedEventCaptor = ArgumentCaptor.forClass(OrderPlacedEvent.class);
        verify(eventPublisher).publishEvent(placedEventCaptor.capture());
        OrderPlacedEvent placedEvent = placedEventCaptor.getValue();
        assertThat(placedEvent.getOrderId()).isEqualTo(orderId);
        assertThat(placedEvent.getPatientId()).isEqualTo(patientId);
        assertThat(placedEvent.getBranchId()).isEqualTo(BRANCH_ID);

        // Step 3: PLACED → PAID
        testOrderService.updateStatus(orderId, OrderStatus.PAID);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PAID);

        // Step 4: PAID → SAMPLE_COLLECTED
        testOrderService.updateStatus(orderId, OrderStatus.SAMPLE_COLLECTED);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.SAMPLE_COLLECTED);

        // Step 5: SAMPLE_COLLECTED → IN_PROGRESS
        testOrderService.updateStatus(orderId, OrderStatus.IN_PROGRESS);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.IN_PROGRESS);

        // Step 6: IN_PROGRESS → RESULTED
        testOrderService.updateStatus(orderId, OrderStatus.RESULTED);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.RESULTED);

        // Step 7: RESULTED → AUTHORISED
        testOrderService.updateStatus(orderId, OrderStatus.AUTHORISED);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.AUTHORISED);

        // Step 8: AUTHORISED → COMPLETED
        testOrderService.updateStatus(orderId, OrderStatus.COMPLETED);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.COMPLETED);
        assertThat(order.getCompletedAt()).isNotNull();
    }

    @Test
    @DisplayName("Order cancellation after placement publishes OrderCancelledEvent and cancels all line items")
    void orderCancellation_publishesEvent() {
        UUID orderId = UUID.randomUUID();

        TestOrder order = new TestOrder();
        order.setId(orderId);
        order.setPatientId(UUID.randomUUID());
        order.setStatus(OrderStatus.DRAFT);

        OrderLineItem item1 = new OrderLineItem();
        item1.setTestCode("CBC");
        item1.setTestName("Complete Blood Count");
        item1.setStatus(OrderLineItemStatus.PENDING);
        item1.setIsActive(true);

        OrderLineItem item2 = new OrderLineItem();
        item2.setTestCode("LFT");
        item2.setTestName("Liver Function Test");
        item2.setStatus(OrderLineItemStatus.PENDING);
        item2.setIsActive(true);

        order.getLineItems().add(item1);
        order.getLineItems().add(item2);

        TestOrderResponse response = new TestOrderResponse();

        when(testOrderRepository.findByIdAndBranchIdAndIsDeletedFalse(orderId, BRANCH_ID))
                .thenReturn(Optional.of(order));
        when(testOrderRepository.save(order)).thenReturn(order);
        when(testOrderMapper.toResponse(order)).thenReturn(response);

        // Place the order first
        testOrderService.placeOrder(orderId);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PLACED);

        // Cancel with reason
        String cancelReason = "Patient request - duplicate order";
        testOrderService.cancelOrder(orderId, cancelReason);

        // Verify OrderCancelledEvent published
        ArgumentCaptor<Object> eventCaptor = ArgumentCaptor.forClass(Object.class);
        verify(eventPublisher, times(2)).publishEvent(eventCaptor.capture());

        OrderCancelledEvent cancelledEvent = eventCaptor.getAllValues().stream()
                .filter(e -> e instanceof OrderCancelledEvent)
                .map(e -> (OrderCancelledEvent) e)
                .findFirst().orElseThrow();

        assertThat(cancelledEvent.getOrderId()).isEqualTo(orderId);
        assertThat(cancelledEvent.getCancelReason()).isEqualTo(cancelReason);
        assertThat(cancelledEvent.getBranchId()).isEqualTo(BRANCH_ID);

        // Verify all line items set to CANCELLED
        assertThat(item1.getStatus()).isEqualTo(OrderLineItemStatus.CANCELLED);
        assertThat(item2.getStatus()).isEqualTo(OrderLineItemStatus.CANCELLED);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELLED);
        assertThat(order.getCancelledAt()).isNotNull();
    }

    @Test
    @DisplayName("PaymentReceivedEvent transitions order from PLACED to PAID when fully paid")
    void paymentReceivedEvent_transitionsPlacedToPaid() {
        UUID orderId = UUID.randomUUID();
        UUID invoiceId = UUID.randomUUID();

        TestOrder order = new TestOrder();
        order.setId(orderId);
        order.setStatus(OrderStatus.PLACED);

        when(testOrderRepository.findByIdAndBranchIdAndIsDeletedFalse(orderId, BRANCH_ID))
                .thenReturn(Optional.of(order));

        PaymentReceivedEvent event = new PaymentReceivedEvent(
                invoiceId, orderId, BRANCH_ID, new BigDecimal("1000.00"), true);

        orderEventListener.handlePaymentReceived(event);

        assertThat(order.getStatus()).isEqualTo(OrderStatus.PAID);
        verify(testOrderRepository).save(order);
    }

    @Test
    @DisplayName("PaymentReceivedEvent with partial payment does not transition order status")
    void paymentReceivedEvent_partialPayment_noTransition() {
        UUID orderId = UUID.randomUUID();
        UUID invoiceId = UUID.randomUUID();

        PaymentReceivedEvent event = new PaymentReceivedEvent(
                invoiceId, orderId, BRANCH_ID, new BigDecimal("500.00"), false);

        orderEventListener.handlePaymentReceived(event);

        verify(testOrderRepository, never()).save(any(TestOrder.class));
    }

    @Test
    @DisplayName("SampleCollectedEvent transitions order from PAID to SAMPLE_COLLECTED")
    void sampleCollectedEvent_transitionsPaidToSampleCollected() {
        UUID orderId = UUID.randomUUID();
        UUID sampleId = UUID.randomUUID();
        UUID patientId = UUID.randomUUID();

        TestOrder order = new TestOrder();
        order.setId(orderId);
        order.setStatus(OrderStatus.PAID);

        when(testOrderRepository.findByIdAndBranchIdAndIsDeletedFalse(orderId, BRANCH_ID))
                .thenReturn(Optional.of(order));

        SampleCollectedEvent event = new SampleCollectedEvent(
                sampleId, orderId, patientId, BRANCH_ID);

        orderEventListener.handleSampleCollected(event);

        assertThat(order.getStatus()).isEqualTo(OrderStatus.SAMPLE_COLLECTED);
        verify(testOrderRepository).save(order);
    }

    @Test
    @DisplayName("Invalid state transitions throw BusinessRuleException")
    void invalidStateTransition_throwsBusinessRuleException() {
        UUID orderId = UUID.randomUUID();

        // DRAFT → PAID is invalid (must go through PLACED first)
        TestOrder draftOrder = new TestOrder();
        draftOrder.setStatus(OrderStatus.DRAFT);
        when(testOrderRepository.findByIdAndBranchIdAndIsDeletedFalse(orderId, BRANCH_ID))
                .thenReturn(Optional.of(draftOrder));

        assertThatThrownBy(() -> testOrderService.updateStatus(orderId, OrderStatus.PAID))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("Invalid state transition");

        // COMPLETED → CANCELLED is invalid (COMPLETED is a terminal state)
        UUID completedOrderId = UUID.randomUUID();
        TestOrder completedOrder = new TestOrder();
        completedOrder.setStatus(OrderStatus.COMPLETED);
        when(testOrderRepository.findByIdAndBranchIdAndIsDeletedFalse(completedOrderId, BRANCH_ID))
                .thenReturn(Optional.of(completedOrder));

        assertThatThrownBy(() -> testOrderService.cancelOrder(completedOrderId, "Too late"))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("COMPLETED");
    }

    @Test
    @DisplayName("validateOrder with multiple tube types returns correct sample groups")
    void validateOrder_withLineItems_returnsSampleGroups() {
        UUID orderId = UUID.randomUUID();
        TestOrder order = new TestOrder();
        order.setPatientId(UUID.randomUUID());

        OrderLineItem edtaItem1 = new OrderLineItem();
        edtaItem1.setTestCode("CBC");
        edtaItem1.setTestName("Complete Blood Count");
        edtaItem1.setSampleType("Blood");
        edtaItem1.setTubeType("EDTA");
        edtaItem1.setIsActive(true);
        edtaItem1.setIsUrgent(false);

        OrderLineItem edtaItem2 = new OrderLineItem();
        edtaItem2.setTestCode("HB");
        edtaItem2.setTestName("Hemoglobin");
        edtaItem2.setSampleType("Blood");
        edtaItem2.setTubeType("EDTA");
        edtaItem2.setIsActive(true);
        edtaItem2.setIsUrgent(false);

        OrderLineItem sstItem = new OrderLineItem();
        sstItem.setTestCode("LFT");
        sstItem.setTestName("Liver Function Test");
        sstItem.setSampleType("Blood");
        sstItem.setTubeType("SST");
        sstItem.setIsActive(true);
        sstItem.setIsUrgent(false);

        order.getLineItems().add(edtaItem1);
        order.getLineItems().add(edtaItem2);
        order.getLineItems().add(sstItem);

        when(testOrderRepository.findByIdAndBranchIdAndIsDeletedFalse(orderId, BRANCH_ID))
                .thenReturn(Optional.of(order));

        OrderValidationResponse result = testOrderService.validateOrder(orderId);

        assertThat(result.isValid()).isTrue();
        assertThat(result.getErrors()).isEmpty();
        assertThat(result.getSampleGroups()).hasSize(2);

        SampleGroupResponse edtaGroup = result.getSampleGroups().stream()
                .filter(g -> "EDTA".equals(g.getTubeType()))
                .findFirst().orElseThrow();
        assertThat(edtaGroup.getSampleType()).isEqualTo("Blood");
        assertThat(edtaGroup.getTestCodes()).containsExactly("CBC", "HB");
        assertThat(edtaGroup.getTestNames()).containsExactly("Complete Blood Count", "Hemoglobin");

        SampleGroupResponse sstGroup = result.getSampleGroups().stream()
                .filter(g -> "SST".equals(g.getTubeType()))
                .findFirst().orElseThrow();
        assertThat(sstGroup.getTestCodes()).containsExactly("LFT");
        assertThat(sstGroup.getTestNames()).containsExactly("Liver Function Test");
    }

    @Test
    @DisplayName("Turnaround time calculation returns max TAT from active line items")
    void turnaroundTimeCalculation_returnsMaxTat() {
        UUID orderId = UUID.randomUUID();
        TestOrder order = new TestOrder();

        OrderLineItem item1 = new OrderLineItem();
        item1.setTurnaroundTimeHours(2);
        item1.setIsActive(true);

        OrderLineItem item2 = new OrderLineItem();
        item2.setTurnaroundTimeHours(4);
        item2.setIsActive(true);

        OrderLineItem item3 = new OrderLineItem();
        item3.setTurnaroundTimeHours(6);
        item3.setIsActive(true);

        order.getLineItems().add(item1);
        order.getLineItems().add(item2);
        order.getLineItems().add(item3);

        when(testOrderRepository.findByIdAndBranchIdAndIsDeletedFalse(orderId, BRANCH_ID))
                .thenReturn(Optional.of(order));

        Integer tat = testOrderService.calculateTurnaroundTime(orderId);

        assertThat(tat).isEqualTo(6);
    }
}
