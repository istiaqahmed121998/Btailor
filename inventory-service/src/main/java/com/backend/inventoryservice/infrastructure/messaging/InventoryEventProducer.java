package com.backend.inventoryservice.infrastructure.messaging;

import com.backend.common.events.ReserveInventoryRequest;
import org.springframework.kafka.core.KafkaTemplate;

public class InventoryEventProducer {
    private final KafkaTemplate<String, ReserveInventoryRequest> kafkaTemplate;

    public InventoryEventProducer(KafkaTemplate<String, ReserveInventoryRequest> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendReserveRequest(ReserveInventoryRequest request) {
        kafkaTemplate.send("inventory.reserve.request", request.orderId(), request);
    }
}