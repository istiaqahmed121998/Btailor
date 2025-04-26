package com.backend.inventoryservice.infrastructure.messaging;

import com.backend.common.events.ReserveInventoryResponseEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class InventoryEventProducer {
    private final KafkaTemplate<String, ReserveInventoryResponseEvent> kafkaTemplate;

    public InventoryEventProducer(KafkaTemplate<String, ReserveInventoryResponseEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(ReserveInventoryResponseEvent request) {
        kafkaTemplate.send("InventoryReservedEvent", request.orderId(), request);
    }
}