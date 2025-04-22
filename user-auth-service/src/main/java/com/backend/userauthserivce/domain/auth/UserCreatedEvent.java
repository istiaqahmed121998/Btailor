package com.backend.userauthserivce.domain.auth;

public record UserCreatedEvent(Long userId, String email, String name) {
}
