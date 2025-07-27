package com.backend.common.events;

public record OtpGeneratedEvent(Long userId, String email, String otpCode) {
}