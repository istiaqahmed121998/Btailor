package com.backend.inventoryservice.application.mapper;

import com.backend.inventoryservice.application.dto.InventoryItemRequest;
import com.backend.inventoryservice.application.dto.InventoryItemResponse;
import com.backend.inventoryservice.domain.model.InventoryItem;

public class InventoryItemMapper {

    public static InventoryItem toEntity(InventoryItemRequest request) {
        return new InventoryItem(request.variantSku(), request.availableQuantity());
    }

    public static InventoryItemResponse toResponse(InventoryItem item) {
        return new InventoryItemResponse(
                item.getId(),
                item.getVariantSku(),
                item.getAvailableQuantity(),
                item.getReservedQuantity(),
                item.getLastUpdated()
        );
    }
}