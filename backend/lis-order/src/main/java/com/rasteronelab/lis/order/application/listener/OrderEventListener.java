package com.rasteronelab.lis.order.application.listener;

import com.rasteronelab.lis.core.event.PaymentReceivedEvent;
import com.rasteronelab.lis.core.event.SampleCollectedEvent;
import com.rasteronelab.lis.core.infrastructure.BranchContextHolder;
import com.rasteronelab.lis.order.domain.model.OrderStatus;
import com.rasteronelab.lis.order.domain.repository.TestOrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Listens for cross-module events and updates order status accordingly.
 * - PaymentReceivedEvent → PLACED to PAID (when fully paid)
 * - SampleCollectedEvent → PAID to SAMPLE_COLLECTED
 */
@Component
public class OrderEventListener {

    private static final Logger log = LoggerFactory.getLogger(OrderEventListener.class);

    private final TestOrderRepository testOrderRepository;

    public OrderEventListener(TestOrderRepository testOrderRepository) {
        this.testOrderRepository = testOrderRepository;
    }

    @EventListener
    @Transactional
    public void handlePaymentReceived(PaymentReceivedEvent event) {
        if (!event.isFullyPaid()) {
            log.debug("Partial payment received for order {}; not transitioning status", event.getOrderId());
            return;
        }

        BranchContextHolder.setCurrentBranchId(event.getBranchId());
        try {
            testOrderRepository.findByIdAndBranchIdAndIsDeletedFalse(event.getOrderId(), event.getBranchId())
                    .ifPresent(order -> {
                        if (order.getStatus() == OrderStatus.PLACED) {
                            order.setStatus(OrderStatus.PAID);
                            testOrderRepository.save(order);
                            log.info("Order {} transitioned to PAID after full payment", event.getOrderId());
                        }
                    });
        } finally {
            BranchContextHolder.clear();
        }
    }

    @EventListener
    @Transactional
    public void handleSampleCollected(SampleCollectedEvent event) {
        BranchContextHolder.setCurrentBranchId(event.getBranchId());
        try {
            testOrderRepository.findByIdAndBranchIdAndIsDeletedFalse(event.getOrderId(), event.getBranchId())
                    .ifPresent(order -> {
                        if (order.getStatus() == OrderStatus.PAID) {
                            order.setStatus(OrderStatus.SAMPLE_COLLECTED);
                            testOrderRepository.save(order);
                            log.info("Order {} transitioned to SAMPLE_COLLECTED", event.getOrderId());
                        }
                    });
        } finally {
            BranchContextHolder.clear();
        }
    }
}
