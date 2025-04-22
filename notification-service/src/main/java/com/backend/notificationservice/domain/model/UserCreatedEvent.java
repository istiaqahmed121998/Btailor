package com.backend.notificationservice.domain.model;

public record UserCreatedEvent(Long userId,String email,String name) {
}
