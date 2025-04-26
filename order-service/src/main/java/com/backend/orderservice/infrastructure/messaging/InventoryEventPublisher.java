package com.backend.orderservice.infrastructure.messaging;

import com.backend.common.events.OrderCreatedEvent;
import com.backend.common.events.ReserveInventoryRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class InventoryEventPublisher {
    private final KafkaTemplate<String, ReserveInventoryRequest> kafkaTemplate;

    public InventoryEventPublisher(KafkaTemplate<String, ReserveInventoryRequest> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(ReserveInventoryRequest event) {
        kafkaTemplate.send("OrderCreatedEvent", event);
    }
}