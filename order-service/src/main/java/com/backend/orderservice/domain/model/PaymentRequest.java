package com.backend.orderservice.domain.model;


public record PaymentRequest(
        String orderId,
        long   userId,
        double amount,
        String method
) {}
