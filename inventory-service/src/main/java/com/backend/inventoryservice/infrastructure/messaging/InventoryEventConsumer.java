package com.backend.inventoryservice.infrastructure.messaging;

import com.backend.common.dto.OrderItem;
import com.backend.common.events.ReserveInventoryRequest;
import com.backend.common.events.ReserveInventoryResponseEvent;
import com.backend.inventoryservice.application.InventoryApplicationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class InventoryEventConsumer {
    private final InventoryApplicationService inventoryApplicationService;
    private final KafkaTemplate<String, ReserveInventoryResponseEvent> kafkaTemplate;
    public InventoryEventConsumer(InventoryApplicationService inventoryApplicationService, KafkaTemplate<String, ReserveInventoryResponseEvent> kafkaTemplate) {
        this.inventoryApplicationService = inventoryApplicationService;
        this.kafkaTemplate = kafkaTemplate;
    }
    @KafkaListener(topics = "inventory.reserve.request", groupId = "inventory-service")
    public void consumeReserveRequest(ReserveInventoryRequest request) {
        boolean success = checkInventoryAndReserve(request.productQuantities());
        if (success) {
            for (Map.Entry<OrderItem, Integer> entry : request.productQuantities().entrySet()) {
                OrderItem orderItem = entry.getKey();
                int quantity = entry.getValue();
                inventoryApplicationService.reserveStock(orderItem.getVariantSku(), quantity);
            }
        }
        ReserveInventoryResponseEvent response = new ReserveInventoryResponseEvent(
                request.orderId(), success, success ? "Reserved" : "Out of stock"
        );
        kafkaTemplate.send("inventory.reserve.response", request.orderId(), response);
    }

    private boolean checkInventoryAndReserve(Map<OrderItem, Integer> productQuantities) {
        for (Map.Entry<OrderItem, Integer> entry : productQuantities.entrySet()) {
            OrderItem orderItem = entry.getKey();
            int quantity = entry.getValue();

            boolean isAvailable = inventoryApplicationService.isAvailable(orderItem.getVariantSku(), quantity);
            if (!isAvailable) {
                return false;
            }
        }
        return true;
    }
}
