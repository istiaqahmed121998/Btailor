package com.backend.inventoryservice.application.dto;

public record InventoryItemRequest(String variantSku, Integer availableQuantity) {
}
