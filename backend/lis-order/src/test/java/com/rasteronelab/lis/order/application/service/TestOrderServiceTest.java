package com.rasteronelab.lis.order.application.service;

import com.rasteronelab.lis.core.common.exception.BusinessRuleException;
import com.rasteronelab.lis.core.common.exception.NotFoundException;
import com.rasteronelab.lis.core.infrastructure.BranchContextHolder;
import com.rasteronelab.lis.order.api.dto.OrderLineItemRequest;
import com.rasteronelab.lis.order.api.dto.OrderValidationResponse;
import com.rasteronelab.lis.order.api.dto.SampleGroupResponse;
import com.rasteronelab.lis.order.api.dto.TestOrderRequest;
import com.rasteronelab.lis.order.api.dto.TestOrderResponse;
import com.rasteronelab.lis.order.api.mapper.OrderLineItemMapper;
import com.rasteronelab.lis.order.api.mapper.TestOrderMapper;
import com.rasteronelab.lis.order.domain.model.OrderLineItem;
import com.rasteronelab.lis.order.domain.model.OrderStatus;
import com.rasteronelab.lis.order.domain.model.TestOrder;
import com.rasteronelab.lis.order.domain.repository.TestOrderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("TestOrderService")
@ExtendWith(MockitoExtension.class)
class TestOrderServiceTest {

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

    @BeforeEach
    void setUp() {
        BranchContextHolder.setCurrentBranchId(BRANCH_ID);
    }

    @AfterEach
    void tearDown() {
        BranchContextHolder.clear();
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("create should save order with DRAFT status and generated order number")
    void create_shouldSaveOrderSuccessfully() {
        UUID patientId = UUID.randomUUID();
        TestOrderRequest request = new TestOrderRequest();
        request.setPatientId(patientId);
        request.setPriority("ROUTINE");
        request.setLineItems(Collections.emptyList());

        TestOrder order = new TestOrder();
        TestOrder saved = new TestOrder();
        TestOrderResponse response = new TestOrderResponse();

        when(testOrderMapper.toEntity(request)).thenReturn(order);
        when(testOrderRepository.countByBranchIdAndIsDeletedFalseAndOrderDateBetween(eq(BRANCH_ID), any(), any())).thenReturn(0L);
        when(testOrderRepository.save(order)).thenReturn(saved);
        when(testOrderMapper.toResponse(saved)).thenReturn(response);

        TestOrderResponse result = testOrderService.create(request);

        assertThat(result).isEqualTo(response);
        assertThat(order.getBranchId()).isEqualTo(BRANCH_ID);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.DRAFT);
        assertThat(order.getOrderNumber()).startsWith("ORD-");
        verify(testOrderRepository).save(order);
    }

    @Test
    @DisplayName("create with line items should process and attach items")
    void create_withLineItems_shouldProcessAndAttachItems() {
        UUID patientId = UUID.randomUUID();
        UUID testId = UUID.randomUUID();

        OrderLineItemRequest itemRequest = new OrderLineItemRequest();
        itemRequest.setTestId(testId);
        itemRequest.setTestCode("CBC");
        itemRequest.setTestName("Complete Blood Count");
        itemRequest.setUnitPrice(new BigDecimal("100.00"));
        itemRequest.setDiscountAmount(new BigDecimal("10.00"));

        TestOrderRequest request = new TestOrderRequest();
        request.setPatientId(patientId);
        request.setLineItems(Collections.singletonList(itemRequest));

        TestOrder order = new TestOrder();
        OrderLineItem lineItem = new OrderLineItem();
        lineItem.setUnitPrice(new BigDecimal("100.00"));
        lineItem.setDiscountAmount(new BigDecimal("10.00"));
        TestOrder saved = new TestOrder();
        TestOrderResponse response = new TestOrderResponse();

        when(testOrderMapper.toEntity(request)).thenReturn(order);
        when(orderLineItemMapper.toEntity(itemRequest)).thenReturn(lineItem);
        when(testOrderRepository.countByBranchIdAndIsDeletedFalseAndOrderDateBetween(eq(BRANCH_ID), any(), any())).thenReturn(0L);
        when(testOrderRepository.save(order)).thenReturn(saved);
        when(testOrderMapper.toResponse(saved)).thenReturn(response);

        TestOrderResponse result = testOrderService.create(request);

        assertThat(result).isEqualTo(response);
        assertThat(order.getLineItems()).hasSize(1);
        assertThat(lineItem.getNetPrice()).isEqualByComparingTo(new BigDecimal("90.00"));
    }

    @Test
    @DisplayName("getById should return order")
    void getById_shouldReturnOrder() {
        UUID id = UUID.randomUUID();
        TestOrder order = new TestOrder();
        TestOrderResponse response = new TestOrderResponse();

        when(testOrderRepository.findByIdAndBranchIdAndIsDeletedFalse(id, BRANCH_ID)).thenReturn(Optional.of(order));
        when(testOrderMapper.toResponse(order)).thenReturn(response);

        TestOrderResponse result = testOrderService.getById(id);

        assertThat(result).isEqualTo(response);
    }

    @Test
    @DisplayName("getById not found should throw NotFoundException")
    void getById_notFound_shouldThrowNotFoundException() {
        UUID id = UUID.randomUUID();

        when(testOrderRepository.findByIdAndBranchIdAndIsDeletedFalse(id, BRANCH_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> testOrderService.getById(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("TestOrder");
    }

    @Test
    @DisplayName("placeOrder should transition from DRAFT to PLACED")
    void placeOrder_shouldTransitionFromDraftToPlaced() {
        UUID id = UUID.randomUUID();
        TestOrder order = new TestOrder();
        order.setStatus(OrderStatus.DRAFT);
        TestOrder saved = new TestOrder();
        TestOrderResponse response = new TestOrderResponse();

        when(testOrderRepository.findByIdAndBranchIdAndIsDeletedFalse(id, BRANCH_ID)).thenReturn(Optional.of(order));
        when(testOrderRepository.save(order)).thenReturn(saved);
        when(testOrderMapper.toResponse(saved)).thenReturn(response);

        TestOrderResponse result = testOrderService.placeOrder(id);

        assertThat(result).isEqualTo(response);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PLACED);
    }

    @Test
    @DisplayName("placeOrder from non-DRAFT status should throw BusinessRuleException")
    void placeOrder_fromNonDraftStatus_shouldThrowBusinessRuleException() {
        UUID id = UUID.randomUUID();
        TestOrder order = new TestOrder();
        order.setStatus(OrderStatus.PLACED);

        when(testOrderRepository.findByIdAndBranchIdAndIsDeletedFalse(id, BRANCH_ID)).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> testOrderService.placeOrder(id))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("DRAFT");
    }

    @Test
    @DisplayName("cancelOrder should transition to CANCELLED with reason")
    void cancelOrder_shouldTransitionToCancelled() {
        UUID id = UUID.randomUUID();
        TestOrder order = new TestOrder();
        order.setStatus(OrderStatus.DRAFT);
        TestOrder saved = new TestOrder();
        TestOrderResponse response = new TestOrderResponse();

        when(testOrderRepository.findByIdAndBranchIdAndIsDeletedFalse(id, BRANCH_ID)).thenReturn(Optional.of(order));
        when(testOrderRepository.save(order)).thenReturn(saved);
        when(testOrderMapper.toResponse(saved)).thenReturn(response);

        TestOrderResponse result = testOrderService.cancelOrder(id, "Patient request");

        assertThat(result).isEqualTo(response);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELLED);
        assertThat(order.getCancelReason()).isEqualTo("Patient request");
        assertThat(order.getCancelledAt()).isNotNull();
    }

    @Test
    @DisplayName("cancelOrder from COMPLETED status should throw BusinessRuleException")
    void cancelOrder_fromCompletedStatus_shouldThrowBusinessRuleException() {
        UUID id = UUID.randomUUID();
        TestOrder order = new TestOrder();
        order.setStatus(OrderStatus.COMPLETED);

        when(testOrderRepository.findByIdAndBranchIdAndIsDeletedFalse(id, BRANCH_ID)).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> testOrderService.cancelOrder(id, "Some reason"))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("COMPLETED");
    }

    @Test
    @DisplayName("cancelOrder from CANCELLED status should throw BusinessRuleException")
    void cancelOrder_fromCancelledStatus_shouldThrowBusinessRuleException() {
        UUID id = UUID.randomUUID();
        TestOrder order = new TestOrder();
        order.setStatus(OrderStatus.CANCELLED);

        when(testOrderRepository.findByIdAndBranchIdAndIsDeletedFalse(id, BRANCH_ID)).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> testOrderService.cancelOrder(id, "Some reason"))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("CANCELLED");
    }

    @Test
    @DisplayName("delete should soft delete order")
    void delete_shouldSoftDelete() {
        UUID id = UUID.randomUUID();
        TestOrder order = new TestOrder();
        SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken("admin", null));

        when(testOrderRepository.findByIdAndBranchIdAndIsDeletedFalse(id, BRANCH_ID)).thenReturn(Optional.of(order));

        testOrderService.delete(id);

        assertThat(order.getIsDeleted()).isTrue();
        assertThat(order.getDeletedAt()).isNotNull();
        verify(testOrderRepository).save(order);
    }

    @Test
    @DisplayName("updateStatus should transition SAMPLE_COLLECTED to IN_PROGRESS")
    void updateStatus_shouldTransitionSampleCollectedToInProgress() {
        UUID id = UUID.randomUUID();
        TestOrder order = new TestOrder();
        order.setStatus(OrderStatus.SAMPLE_COLLECTED);
        TestOrder saved = new TestOrder();
        TestOrderResponse response = new TestOrderResponse();

        when(testOrderRepository.findByIdAndBranchIdAndIsDeletedFalse(id, BRANCH_ID)).thenReturn(Optional.of(order));
        when(testOrderRepository.save(order)).thenReturn(saved);
        when(testOrderMapper.toResponse(saved)).thenReturn(response);

        TestOrderResponse result = testOrderService.updateStatus(id, OrderStatus.IN_PROGRESS);

        assertThat(result).isEqualTo(response);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.IN_PROGRESS);
    }

    @Test
    @DisplayName("updateStatus should set completedAt when transitioning to COMPLETED")
    void updateStatus_toCompleted_shouldSetCompletedAt() {
        UUID id = UUID.randomUUID();
        TestOrder order = new TestOrder();
        order.setStatus(OrderStatus.AUTHORISED);
        TestOrder saved = new TestOrder();
        TestOrderResponse response = new TestOrderResponse();

        when(testOrderRepository.findByIdAndBranchIdAndIsDeletedFalse(id, BRANCH_ID)).thenReturn(Optional.of(order));
        when(testOrderRepository.save(order)).thenReturn(saved);
        when(testOrderMapper.toResponse(saved)).thenReturn(response);

        testOrderService.updateStatus(id, OrderStatus.COMPLETED);

        assertThat(order.getStatus()).isEqualTo(OrderStatus.COMPLETED);
        assertThat(order.getCompletedAt()).isNotNull();
    }

    @Test
    @DisplayName("updateStatus should throw for invalid transition")
    void updateStatus_invalidTransition_shouldThrow() {
        UUID id = UUID.randomUUID();
        TestOrder order = new TestOrder();
        order.setStatus(OrderStatus.DRAFT);

        when(testOrderRepository.findByIdAndBranchIdAndIsDeletedFalse(id, BRANCH_ID)).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> testOrderService.updateStatus(id, OrderStatus.COMPLETED))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("Invalid state transition");
    }

    @Test
    @DisplayName("placeOrder should publish OrderPlacedEvent")
    void placeOrder_shouldPublishOrderPlacedEvent() {
        UUID id = UUID.randomUUID();
        TestOrder order = new TestOrder();
        order.setStatus(OrderStatus.DRAFT);
        order.setPatientId(UUID.randomUUID());
        TestOrder saved = new TestOrder();
        saved.setId(id);
        saved.setPatientId(order.getPatientId());
        TestOrderResponse response = new TestOrderResponse();

        when(testOrderRepository.findByIdAndBranchIdAndIsDeletedFalse(id, BRANCH_ID)).thenReturn(Optional.of(order));
        when(testOrderRepository.save(order)).thenReturn(saved);
        when(testOrderMapper.toResponse(saved)).thenReturn(response);

        testOrderService.placeOrder(id);

        verify(eventPublisher).publishEvent(any(com.rasteronelab.lis.core.event.OrderPlacedEvent.class));
    }

    @Test
    @DisplayName("cancelOrder should publish OrderCancelledEvent")
    void cancelOrder_shouldPublishOrderCancelledEvent() {
        UUID id = UUID.randomUUID();
        TestOrder order = new TestOrder();
        order.setStatus(OrderStatus.PLACED);
        TestOrder saved = new TestOrder();
        saved.setId(id);
        TestOrderResponse response = new TestOrderResponse();

        when(testOrderRepository.findByIdAndBranchIdAndIsDeletedFalse(id, BRANCH_ID)).thenReturn(Optional.of(order));
        when(testOrderRepository.save(order)).thenReturn(saved);
        when(testOrderMapper.toResponse(saved)).thenReturn(response);

        testOrderService.cancelOrder(id, "Test reason");

        verify(eventPublisher).publishEvent(any(com.rasteronelab.lis.core.event.OrderCancelledEvent.class));
    }

    @Test
    @DisplayName("validateOrder should return valid when order has line items")
    void validateOrder_shouldReturnValidWhenOrderHasLineItems() {
        UUID id = UUID.randomUUID();
        TestOrder order = new TestOrder();
        order.setPatientId(UUID.randomUUID());

        OrderLineItem lineItem = new OrderLineItem();
        lineItem.setTestCode("CBC");
        lineItem.setTestName("Complete Blood Count");
        lineItem.setSampleType("Blood");
        lineItem.setTubeType("EDTA");
        lineItem.setIsActive(true);
        order.getLineItems().add(lineItem);

        when(testOrderRepository.findByIdAndBranchIdAndIsDeletedFalse(id, BRANCH_ID)).thenReturn(Optional.of(order));

        OrderValidationResponse result = testOrderService.validateOrder(id);

        assertThat(result.isValid()).isTrue();
        assertThat(result.getErrors()).isEmpty();
        assertThat(result.getSampleGroups()).isNotEmpty();
    }

    @Test
    @DisplayName("validateOrder should return invalid when no line items")
    void validateOrder_shouldReturnInvalidWhenNoLineItems() {
        UUID id = UUID.randomUUID();
        TestOrder order = new TestOrder();
        order.setPatientId(UUID.randomUUID());

        when(testOrderRepository.findByIdAndBranchIdAndIsDeletedFalse(id, BRANCH_ID)).thenReturn(Optional.of(order));

        OrderValidationResponse result = testOrderService.validateOrder(id);

        assertThat(result.isValid()).isFalse();
        assertThat(result.getErrors()).contains("Order must have at least one line item");
        assertThat(result.getSampleGroups()).isEmpty();
    }

    @Test
    @DisplayName("getSampleGroups should group by tube type")
    void getSampleGroups_shouldGroupByTubeType() {
        UUID id = UUID.randomUUID();
        TestOrder order = new TestOrder();

        OrderLineItem item1 = new OrderLineItem();
        item1.setTestCode("CBC");
        item1.setTestName("Complete Blood Count");
        item1.setSampleType("Blood");
        item1.setTubeType("EDTA");
        item1.setIsActive(true);
        item1.setIsUrgent(false);

        OrderLineItem item2 = new OrderLineItem();
        item2.setTestCode("ESR");
        item2.setTestName("Erythrocyte Sedimentation Rate");
        item2.setSampleType("Blood");
        item2.setTubeType("EDTA");
        item2.setIsActive(true);
        item2.setIsUrgent(false);

        OrderLineItem item3 = new OrderLineItem();
        item3.setTestCode("PT");
        item3.setTestName("Prothrombin Time");
        item3.setSampleType("Blood");
        item3.setTubeType("Citrate");
        item3.setIsActive(true);
        item3.setIsUrgent(true);

        order.getLineItems().add(item1);
        order.getLineItems().add(item2);
        order.getLineItems().add(item3);

        when(testOrderRepository.findByIdAndBranchIdAndIsDeletedFalse(id, BRANCH_ID)).thenReturn(Optional.of(order));

        List<SampleGroupResponse> result = testOrderService.getSampleGroups(id);

        assertThat(result).hasSize(2);

        SampleGroupResponse edtaGroup = result.stream()
                .filter(g -> "EDTA".equals(g.getTubeType()))
                .findFirst().orElseThrow();
        assertThat(edtaGroup.getSampleType()).isEqualTo("Blood");
        assertThat(edtaGroup.getTestCodes()).containsExactly("CBC", "ESR");
        assertThat(edtaGroup.getTestNames()).containsExactly("Complete Blood Count", "Erythrocyte Sedimentation Rate");

        SampleGroupResponse citrateGroup = result.stream()
                .filter(g -> "Citrate".equals(g.getTubeType()))
                .findFirst().orElseThrow();
        assertThat(citrateGroup.getTestCodes()).containsExactly("PT");
        assertThat(citrateGroup.getPriority()).isEqualTo("URGENT");
    }

    @Test
    @DisplayName("getPendingCollectionOrders should return PAID orders")
    void getPendingCollectionOrders_shouldReturnPaidOrders() {
        Pageable pageable = PageRequest.of(0, 10);
        TestOrder order = new TestOrder();
        order.setStatus(OrderStatus.PAID);
        TestOrderResponse response = new TestOrderResponse();
        response.setStatus("PAID");

        Page<TestOrder> page = new PageImpl<>(List.of(order));

        when(testOrderRepository.findAllByBranchIdAndIsDeletedFalseAndStatus(BRANCH_ID, OrderStatus.PAID, pageable))
                .thenReturn(page);
        when(testOrderMapper.toResponse(order)).thenReturn(response);

        Page<TestOrderResponse> result = testOrderService.getPendingCollectionOrders(pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getStatus()).isEqualTo("PAID");
    }

    @Test
    @DisplayName("calculateTurnaroundTime should return max TAT from active line items")
    void calculateTurnaroundTime_shouldReturnMaxTat() {
        UUID id = UUID.randomUUID();
        TestOrder order = new TestOrder();

        OrderLineItem item1 = new OrderLineItem();
        item1.setTurnaroundTimeHours(24);
        item1.setIsActive(true);

        OrderLineItem item2 = new OrderLineItem();
        item2.setTurnaroundTimeHours(48);
        item2.setIsActive(true);

        OrderLineItem item3 = new OrderLineItem();
        item3.setTurnaroundTimeHours(72);
        item3.setIsActive(false);

        order.getLineItems().add(item1);
        order.getLineItems().add(item2);
        order.getLineItems().add(item3);

        when(testOrderRepository.findByIdAndBranchIdAndIsDeletedFalse(id, BRANCH_ID)).thenReturn(Optional.of(order));

        Integer tat = testOrderService.calculateTurnaroundTime(id);

        assertThat(tat).isEqualTo(48);
    }
}
