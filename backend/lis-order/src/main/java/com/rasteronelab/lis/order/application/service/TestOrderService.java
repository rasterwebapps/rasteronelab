package com.rasteronelab.lis.order.application.service;

import com.rasteronelab.lis.core.common.exception.BusinessRuleException;
import com.rasteronelab.lis.core.common.exception.NotFoundException;
import com.rasteronelab.lis.core.event.OrderCancelledEvent;
import com.rasteronelab.lis.core.event.OrderPlacedEvent;
import com.rasteronelab.lis.core.infrastructure.BranchContextHolder;
import com.rasteronelab.lis.core.util.BarcodeGeneratorUtil;
import com.rasteronelab.lis.order.api.dto.OrderLineItemRequest;
import com.rasteronelab.lis.order.api.dto.OrderValidationResponse;
import com.rasteronelab.lis.order.api.dto.SampleGroupResponse;
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
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Service for TestOrder CRUD and state management operations.
 * All queries are branch-scoped via BranchContextHolder.
 */
@Service
@Transactional
public class TestOrderService {

    private static final DateTimeFormatter ORDER_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

    private static final Map<OrderStatus, Set<OrderStatus>> VALID_TRANSITIONS = Map.of(
            OrderStatus.DRAFT, Set.of(OrderStatus.PLACED, OrderStatus.CANCELLED),
            OrderStatus.PLACED, Set.of(OrderStatus.PAID, OrderStatus.CANCELLED),
            OrderStatus.PAID, Set.of(OrderStatus.SAMPLE_COLLECTED, OrderStatus.CANCELLED),
            OrderStatus.SAMPLE_COLLECTED, Set.of(OrderStatus.IN_PROGRESS, OrderStatus.CANCELLED),
            OrderStatus.IN_PROGRESS, Set.of(OrderStatus.RESULTED, OrderStatus.CANCELLED),
            OrderStatus.RESULTED, Set.of(OrderStatus.AUTHORISED, OrderStatus.CANCELLED),
            OrderStatus.AUTHORISED, Set.of(OrderStatus.COMPLETED, OrderStatus.CANCELLED)
    );

    private final TestOrderRepository testOrderRepository;
    private final TestOrderMapper testOrderMapper;
    private final OrderLineItemMapper orderLineItemMapper;
    private final ApplicationEventPublisher eventPublisher;

    public TestOrderResponse create(TestOrderRequest request) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();

        TestOrder order = testOrderMapper.toEntity(request);
        order.setBranchId(branchId);
        order.setStatus(OrderStatus.DRAFT);
        order.setOrderDate(LocalDateTime.now());
        order.setOrderNumber(generateOrderNumber(branchId));
        order.setBarcode(BarcodeGeneratorUtil.generateOrderNumber(
                testOrderRepository.countByBranchIdAndIsDeletedFalseAndOrderDateBetween(
                        branchId, LocalDateTime.now().toLocalDate().atStartOfDay(),
                        LocalDateTime.now().toLocalDate().atStartOfDay().plusDays(1)) + 1));

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

        eventPublisher.publishEvent(new OrderPlacedEvent(
                saved.getId(), saved.getPatientId(), branchId));

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

        eventPublisher.publishEvent(new OrderCancelledEvent(
                saved.getId(), branchId, reason));

        return testOrderMapper.toResponse(saved);
    }

    public TestOrderResponse updateStatus(UUID id, OrderStatus newStatus) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        TestOrder order = testOrderRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("TestOrder", id));

        OrderStatus currentStatus = order.getStatus();
        Set<OrderStatus> allowed = VALID_TRANSITIONS.get(currentStatus);
        if (allowed == null || !allowed.contains(newStatus)) {
            throw new BusinessRuleException("LIS-ORD-003",
                    "Invalid state transition from " + currentStatus + " to " + newStatus);
        }

        order.setStatus(newStatus);
        if (newStatus == OrderStatus.COMPLETED) {
            order.setCompletedAt(LocalDateTime.now());
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

    @Transactional(readOnly = true)
    public OrderValidationResponse validateOrder(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        TestOrder order = testOrderRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("TestOrder", id));

        List<String> errors = new ArrayList<>();

        if (order.getLineItems() == null || order.getLineItems().isEmpty()) {
            errors.add("Order must have at least one line item");
        }

        if (order.getPatientId() == null) {
            errors.add("Order must have a patient assigned");
        }

        boolean valid = errors.isEmpty();
        List<SampleGroupResponse> sampleGroups = valid ? buildSampleGroups(order.getLineItems()) : List.of();

        return OrderValidationResponse.builder()
                .valid(valid)
                .errors(errors)
                .sampleGroups(sampleGroups)
                .build();
    }

    @Transactional(readOnly = true)
    public List<SampleGroupResponse> getSampleGroups(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        TestOrder order = testOrderRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("TestOrder", id));

        return buildSampleGroups(order.getLineItems());
    }

    @Transactional(readOnly = true)
    public Integer calculateTurnaroundTime(UUID id) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        TestOrder order = testOrderRepository.findByIdAndBranchIdAndIsDeletedFalse(id, branchId)
                .orElseThrow(() -> new NotFoundException("TestOrder", id));

        if (order.getLineItems() == null || order.getLineItems().isEmpty()) {
            return 0;
        }

        return order.getLineItems().stream()
                .filter(item -> item.getIsActive() != null && item.getIsActive())
                .map(OrderLineItem::getTurnaroundTimeHours)
                .filter(java.util.Objects::nonNull)
                .max(Integer::compareTo)
                .orElse(0);
    }

    @Transactional(readOnly = true)
    public Page<TestOrderResponse> getPendingCollectionOrders(Pageable pageable) {
        UUID branchId = BranchContextHolder.getCurrentBranchId();
        return testOrderRepository.findAllByBranchIdAndIsDeletedFalseAndStatus(branchId, OrderStatus.PAID, pageable)
                .map(testOrderMapper::toResponse);
    }

    private List<SampleGroupResponse> buildSampleGroups(List<OrderLineItem> lineItems) {
        if (lineItems == null || lineItems.isEmpty()) {
            return List.of();
        }

        Map<String, List<OrderLineItem>> grouped = new LinkedHashMap<>();
        for (OrderLineItem item : lineItems) {
            if (item.getIsActive() == null || !item.getIsActive()) {
                continue;
            }
            String key = normalizeGroupKey(item.getSampleType()) + "|" + normalizeGroupKey(item.getTubeType());
            grouped.computeIfAbsent(key, k -> new ArrayList<>()).add(item);
        }

        List<SampleGroupResponse> result = new ArrayList<>();
        for (Map.Entry<String, List<OrderLineItem>> entry : grouped.entrySet()) {
            List<OrderLineItem> items = entry.getValue();
            OrderLineItem first = items.get(0);

            List<String> testCodes = new ArrayList<>();
            List<String> testNames = new ArrayList<>();
            String highestPriority = "ROUTINE";

            for (OrderLineItem item : items) {
                testCodes.add(item.getTestCode());
                testNames.add(item.getTestName());
                if (Boolean.TRUE.equals(item.getIsUrgent())) {
                    highestPriority = "URGENT";
                }
            }

            result.add(SampleGroupResponse.builder()
                    .sampleType(first.getSampleType())
                    .tubeType(first.getTubeType())
                    .testCodes(testCodes)
                    .testNames(testNames)
                    .priority(highestPriority)
                    .build());
        }
        return result;
    }

    private String normalizeGroupKey(String value) {
        return value != null ? value : "";
    }

    public TestOrderService(TestOrderRepository testOrderRepository, TestOrderMapper testOrderMapper,
                            OrderLineItemMapper orderLineItemMapper, ApplicationEventPublisher eventPublisher) {
        this.testOrderRepository = testOrderRepository;
        this.testOrderMapper = testOrderMapper;
        this.orderLineItemMapper = orderLineItemMapper;
        this.eventPublisher = eventPublisher;
    }

}
