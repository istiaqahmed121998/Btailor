package com.backend.orderservice.infrastructure.messaging;

import com.backend.common.events.PaymentFailedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class PaymentFailedPublisher {
    private final KafkaTemplate<String, PaymentFailedEvent> kafkaTemplate;

    public PaymentFailedPublisher(KafkaTemplate<String, PaymentFailedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(PaymentFailedEvent event) {
        kafkaTemplate.send("PaymentFailed", event);
    }
}
