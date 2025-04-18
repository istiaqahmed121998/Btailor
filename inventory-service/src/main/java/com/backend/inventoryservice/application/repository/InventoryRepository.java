package com.backend.inventoryservice.application.repository;

import com.backend.inventoryservice.application.model.InventoryItem;

import java.util.Optional;

public interface InventoryRepository {
    Optional<InventoryItem> findByProductAndVariant(Long productId, Long variantId);

    InventoryItem save(InventoryItem item);
}
