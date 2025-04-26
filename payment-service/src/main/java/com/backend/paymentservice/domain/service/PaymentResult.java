package com.backend.paymentservice.domain.service;

public record PaymentResult(boolean success, String transactionId, String failureReason) {
}
