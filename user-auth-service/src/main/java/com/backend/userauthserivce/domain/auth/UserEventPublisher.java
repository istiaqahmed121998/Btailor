package com.backend.userauthserivce.domain.auth;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public UserEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishUserCreated(UserCreatedEvent event) {
        kafkaTemplate.send("user-events", event.userId().toString(), event)
                .thenAccept(result -> {
                    System.out.println("✅ Sent user-created event for ID: " + event.userId());
                })
                .exceptionally(ex -> {
                    System.err.println("❌ Failed to send user-created event: " + ex.getMessage());
                    return null;
                });
    }
}