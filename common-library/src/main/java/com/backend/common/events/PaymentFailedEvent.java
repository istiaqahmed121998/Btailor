package com.backend.common.events;

import com.backend.common.dto.OrderItem;

import java.util.List;

public record PaymentFailedEvent(String id,Long buyerId, String buyerName, String buyerEmail, String paymentMethod, double totalAmount, String failureReason, List<OrderItem> items) {}
