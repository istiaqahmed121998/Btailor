package com.backend.cartservice.application;

import com.backend.common.events.CartCheckedOutEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class CheckoutPublisher {

    private final KafkaTemplate<String, CartCheckedOutEvent> kafkaTemplate;

    public CheckoutPublisher(KafkaTemplate<String, CartCheckedOutEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(CartCheckedOutEvent event) {
        kafkaTemplate.send("CartCheckedOutEvent",String.valueOf(event.getUserId()), event);
    }
}