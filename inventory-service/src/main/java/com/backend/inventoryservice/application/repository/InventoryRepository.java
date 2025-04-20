package com.backend.inventoryservice.application.repository;

import com.backend.inventoryservice.application.model.InventoryItem;

import java.util.Optional;

public interface InventoryRepository {
    InventoryItem save(InventoryItem item);

    Optional<InventoryItem> findByVariantSku(String variantSku);
}
