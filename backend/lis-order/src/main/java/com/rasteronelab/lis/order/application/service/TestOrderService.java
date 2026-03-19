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
import com.rasteronelab.lis.order.domain.model.OrderLineItemStatus;
import com.rasteronelab.lis.order.domain.model.OrderStatus;
import com.rasteronelab.lis.order.domain.model.Priority;
import com.rasteronelab.lis.order.domain.model.TestOrder;
import com.rasteronelab.lis.order.domain.repository.TestOrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * Service for TestOrder CRUD and state management operations.
 * All queries are branch-scoped via BranchContextHolder.
 */
@Service
@Transactional
public class TestOrderService {

    private static final DateTimeFormatter ORDER_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

    private final TestOrderRepository testOrderRepository;
    private final TestOrderMapper testOrderMapper;
    private final OrderLineItemMapper orderLineItemMapper;

    public TestOrderResponse create(TestOrderRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        TestOrder order = testOrderMapper.toEntity(request);
        order.setBranchId(branchId);
        order.setStatus(OrderStatus.DRAFT);
        order.setOrderDate(LocalDateTime.now());
        order.setOrderNumber(generateOrderNumber(branchId));

        if (request.getPriority() != null && !request.getPriority().isBlank()) {
            order.setPriority(Priority.valueOf(request.getPriority()));
        } else {
            order.setPriority(Priority.ROUTINE);
        }

        if (request.getLineItems() != null) {
            for (OrderLineItemRequest itemRequest : request.getLineItems()) {
                OrderLineItem lineItem = orderLineItemMapper.toEntity(itemRequest);
                lineItem.setBranchId(branchId);
                lineItem.setOrder(order);
                lineItem.setStatus(OrderLineItemStatus.PENDING);
                lineItem.setIsActive(true);
                calculateNetPrice(lineItem);
                order.getLineItems().add(lineItem);
            }
        }

        TestOrder saved = testOrderRepository.save(order);
        return testOrderMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public TestOrderResponse getById(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        TestOrder order = testOrderRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("TestOrder", id));
        return testOrderMapper.toResponse(order);
    }

    @Transactional(readOnly = true)
    public Page<TestOrderResponse> getAll(Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return testOrderRepository.findAllByBranchIdAndIsDeletedFalse(branchId, pageable)
                .map(testOrderMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<TestOrderResponse> getByPatient(UUID patientId, Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return testOrderRepository.findAllByPatientIdAndBranchIdAndIsDeletedFalse(patientId, branchId, pageable)
                .map(testOrderMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<TestOrderResponse> getByStatus(OrderStatus status, Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return testOrderRepository.findAllByStatusAndBranchIdAndIsDeletedFalse(status, branchId, pageable)
                .map(testOrderMapper::toResponse);
    }

    public TestOrderResponse placeOrder(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        TestOrder order = testOrderRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("TestOrder", id));

        if (order.getStatus() != OrderStatus.DRAFT) {
            throw new BusinessRuleException("LIS-ORD-001",
                    "Order can only be placed from DRAFT status. Current status: " + order.getStatus());
        }

        order.setStatus(OrderStatus.PLACED);
        TestOrder saved = testOrderRepository.save(order);
        return testOrderMapper.toResponse(saved);
    }

    public TestOrderResponse cancelOrder(UUID id, String reason) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        TestOrder order = testOrderRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("TestOrder", id));

        if (order.getStatus() == OrderStatus.COMPLETED || order.getStatus() == OrderStatus.CANCELLED) {
            throw new BusinessRuleException("LIS-ORD-002",
                    "Cannot cancel order in " + order.getStatus() + " status");
        }

        order.setStatus(OrderStatus.CANCELLED);
        order.setCancelledAt(LocalDateTime.now());
        order.setCancelReason(reason);

        for (OrderLineItem lineItem : order.getLineItems()) {
            if (lineItem.getStatus() != OrderLineItemStatus.COMPLETED
                    && lineItem.getStatus() != OrderLineItemStatus.CANCELLED) {
                lineItem.setStatus(OrderLineItemStatus.CANCELLED);
            }
        }

        TestOrder saved = testOrderRepository.save(order);
        return testOrderMapper.toResponse(saved);
    }

    public void delete(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        TestOrder order = testOrderRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("TestOrder", id));

        String currentUser = SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getName()
                : "system";

        order.softDelete(currentUser);
        testOrderRepository.save(order);
    }

    private String generateOrderNumber(UUID branchId) {
        String datePrefix = LocalDateTime.now().format(ORDER_DATE_FORMAT);
        String prefix = "ORD-" + datePrefix + "-";

        LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        long count = testOrderRepository.countByBranchIdAndIsDeletedFalseAndOrderDateBetween(branchId, startOfDay, endOfDay);

        return prefix + String.format("%04d", count + 1);
    }

    private void calculateNetPrice(OrderLineItem lineItem) {
        BigDecimal unitPrice = lineItem.getUnitPrice() != null ? lineItem.getUnitPrice() : BigDecimal.ZERO;
        BigDecimal discount = lineItem.getDiscountAmount() != null ? lineItem.getDiscountAmount() : BigDecimal.ZERO;
        lineItem.setNetPrice(unitPrice.subtract(discount));
    }

    public TestOrderService(TestOrderRepository testOrderRepository, TestOrderMapper testOrderMapper, OrderLineItemMapper orderLineItemMapper) {
        this.testOrderRepository = testOrderRepository;
        this.testOrderMapper = testOrderMapper;
        this.orderLineItemMapper = orderLineItemMapper;
    }

}
