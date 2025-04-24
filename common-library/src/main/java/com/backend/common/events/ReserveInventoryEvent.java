package com.backend.common.events;

import com.backend.common.dto.CartItem;

import java.util.List;

public record ReserveInventoryEvent(String id, Long userId, List<CartItem> items){
}
