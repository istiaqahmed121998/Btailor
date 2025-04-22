package com.backend.notificationservice.domain.model;

public record OrderCreatedEvent (Long userId,String email,String name,String orderId){
}
