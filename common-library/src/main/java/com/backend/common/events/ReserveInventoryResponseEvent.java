package com.backend.common.events;

public record ReserveInventoryResponseEvent(String orderId, boolean success, String reason) {
}

