package com.backend.paymentservice.infrastructure.processor;

import com.backend.paymentservice.domain.service.PaymentProcessor;
import com.backend.paymentservice.domain.service.PaymentResult;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Profile("prod")
public class MockPaymentAdapter implements PaymentProcessor {
    @Override
    public PaymentResult process(String orderId, double amount, String method, long userId) {
        boolean success = method.equals("COD") || Math.random() > 0.3;
        return new PaymentResult(
                success,
                success ? UUID.randomUUID().toString() : null,
                success ? null : "Mock failure"
        );
    }
}
