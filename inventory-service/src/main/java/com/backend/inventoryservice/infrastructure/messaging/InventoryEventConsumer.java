package com.backend.inventoryservice.infrastructure.messaging;

import com.backend.common.dto.CartItem;
import com.backend.common.dto.OrderItem;
import com.backend.common.events.PaymentFailedEvent;
import com.backend.common.events.OrderCompletedEvent;
import com.backend.common.events.ReserveInventoryRequest;
import com.backend.common.events.ReserveInventoryResponseEvent;
import com.backend.inventoryservice.application.InventoryApplicationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryEventConsumer {
    private final InventoryApplicationService inventoryApplicationService;
    private final InventoryEventProducer inventoryEventProducer;
    public InventoryEventConsumer(InventoryApplicationService inventoryApplicationService, InventoryEventProducer inventoryEventProducer) {
        this.inventoryApplicationService = inventoryApplicationService;
        this.inventoryEventProducer = inventoryEventProducer;
    }
    @KafkaListener(topics = "OrderCreatedEvent", groupId = "inventory-service-group",containerFactory = "reserveInventoryRequestListenerFactory")
    public void consumeReserveRequest(ReserveInventoryRequest request) {
        boolean success = checkInventoryAndReserve(request.items());
        if (success) {
            for (CartItem item : request.items()) {
                inventoryApplicationService.reserveStock(item.getVariantSku(), item.getQuantity());
            }
        }
        ReserveInventoryResponseEvent response = new ReserveInventoryResponseEvent(
                request.orderId(), success, success ? "Reserved" : "Out of stock"
        );
        inventoryEventProducer.publish(response);
    }

    private boolean checkInventoryAndReserve(List<CartItem> items) {
        for (CartItem item : items) {
            boolean isAvailable = inventoryApplicationService.isAvailable(item.getVariantSku(), item.getQuantity());
            if (!isAvailable) {
                return false;
            }
        }
        return true;
    }

    @KafkaListener(topics = "OrderCompleted", groupId = "inventory-service-group",containerFactory = "deductInventoryRequestListenerFactory")
    public void consumePaymentSuccessfulRequest(OrderCompletedEvent successfulEvent) {
        deductFromItem(successfulEvent.items());
    }

    @KafkaListener(topics = "PaymentFailed", groupId = "inventory-service-group",containerFactory = "releaseInventoryRequestListenerFactory")
    public void consumePaymentFailedRequest(PaymentFailedEvent failedEvent) {
        releaseFromItem(failedEvent.items());
    }
    private void deductFromItem(List<OrderItem> items) {
        for (OrderItem item : items) {
            inventoryApplicationService.deductStock(item.getVariantSku(), item.getQuantity());
        }
    }
    private void releaseFromItem(List<OrderItem> items) {
        for (OrderItem item : items) {
            inventoryApplicationService.releaseStock(item.getVariantSku(), item.getQuantity());
        }
    }
}
