package com.backend.inventoryservice.infrastructure.messaging;

import com.backend.common.dto.CartItem;
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
    private final KafkaTemplate<String, ReserveInventoryResponseEvent> kafkaTemplate;
    public InventoryEventConsumer(InventoryApplicationService inventoryApplicationService, KafkaTemplate<String, ReserveInventoryResponseEvent> kafkaTemplate) {
        this.inventoryApplicationService = inventoryApplicationService;
        this.kafkaTemplate = kafkaTemplate;
    }
    @KafkaListener(topics = "inventory-reserve-request", groupId = "inventory-service-group",containerFactory = "reserveInventoryRequestListenerFactory")
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
        kafkaTemplate.send("inventory-reserved-response", request.orderId(), response);
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
}
