package com.rasteronelab.lis.order.application.service;

import com.rasteronelab.lis.core.common.exception.BusinessRuleException;
import com.rasteronelab.lis.core.common.exception.NotFoundException;
import com.rasteronelab.lis.core.infrastructure.BranchContextHolder;
import com.rasteronelab.lis.order.api.dto.OrderLineItemRequest;
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
}
