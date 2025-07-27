package com.backend.userauthserivce.domain.auth;

import com.backend.common.events.OtpGeneratedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
@Service
public class ForgetPasswordPublisher {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public ForgetPasswordPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    public void publishOtp(OtpGeneratedEvent event) {
        kafkaTemplate.send("OtpEvents", event.userId().toString(), event)
                .thenAccept(result -> {
                    System.out.println("✅ Sent for event for ID: " + event.userId());
                })
                .exceptionally(ex -> {
                    System.err.println("❌ Failed to send user-created event: " + ex.getMessage());
                    return null;
                });
    }
}
