package com.backend.orderservice.infrastructure.messaging;

import com.backend.common.events.OrderCompletedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderCompletePublisher {
    private final KafkaTemplate<String, OrderCompletedEvent> kafkaTemplate;

    public OrderCompletePublisher(KafkaTemplate<String, OrderCompletedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(OrderCompletedEvent event) {
        kafkaTemplate.send("OrderCompleted", event);
    }
}
