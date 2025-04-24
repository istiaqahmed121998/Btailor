package com.backend.common.events;

public record UserCreatedEvent(Long userId, String name, String email) {
}
