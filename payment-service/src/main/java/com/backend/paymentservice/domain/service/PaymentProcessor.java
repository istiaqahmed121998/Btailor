package com.backend.paymentservice.domain.service;


public interface PaymentProcessor {
    PaymentResult process(String orderId, double amount, String method, long userId);
}