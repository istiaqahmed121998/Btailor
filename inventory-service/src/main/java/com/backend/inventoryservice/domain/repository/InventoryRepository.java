package com.backend.inventoryservice.domain.repository;

import com.backend.inventoryservice.domain.model.InventoryItem;

import java.util.Optional;

public interface InventoryRepository {
    InventoryItem save(InventoryItem item);

    Optional<InventoryItem> findByVariantSku(String variantSku);
}
