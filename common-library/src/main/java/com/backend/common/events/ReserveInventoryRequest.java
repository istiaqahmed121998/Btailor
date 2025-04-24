package com.backend.common.events;

import com.backend.common.dto.CartItem;

import java.util.List;

public record ReserveInventoryRequest(String orderId, Long userId, List<CartItem> items) {}
