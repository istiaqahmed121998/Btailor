package com.backend.cartservice.application;

import com.backend.cartservice.domain.event.CartCheckedOutEvent;
import com.backend.cartservice.domain.model.CartItem;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class CheckoutPublisher {

    private final KafkaTemplate<String, CartCheckedOutEvent> kafkaTemplate;

    public CheckoutPublisher(KafkaTemplate<String, CartCheckedOutEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(Long userId, Long shippingAddressId, String paymentMethod, List<CartItem> items) {
        CartCheckedOutEvent event = new CartCheckedOutEvent(userId, items,shippingAddressId, paymentMethod, Instant.now() );
        kafkaTemplate.send("cart-checked-out",String.valueOf(userId), event);
    }
}