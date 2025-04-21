package com.backend.inventoryservice.application.dto;

import java.time.LocalDateTime;

public record InventoryItemResponse(Long id, String variantSku, Integer availableQuantity,
               Integer reservedQuantity, LocalDateTime lastUpdated) {
}
