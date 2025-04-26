package com.backend.paymentservice.domain.service;

import org.springframework.stereotype.Service;

@Service
public interface PaymentProcessor {
    PaymentResult process(String orderId, double amount, String method, long userId);
}