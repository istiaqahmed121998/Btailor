package com.backend.orderservice.domain.model;

public record PaymentResult(
        boolean success,
        String  transactionId,
        String  failureReason
) {}