package com.backend.common.events;

import com.backend.common.dto.OrderItem;

import java.util.Map;

public record ReserveInventoryRequest(String orderId, Map<OrderItem, Integer> productQuantities) {}
