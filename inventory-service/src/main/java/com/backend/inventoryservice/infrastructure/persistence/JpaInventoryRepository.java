package com.backend.inventoryservice.infrastructure.persistence;

import com.backend.inventoryservice.application.model.InventoryItem;
import com.backend.inventoryservice.application.repository.InventoryRepository;

import java.util.Optional;

public class JpaInventoryRepository implements InventoryRepository {
    @Override
    public Optional<InventoryItem> findByProductAndVariant(Long productId, Long variantId) {
        return Optional.empty();
    }

    @Override
    public InventoryItem save(InventoryItem item) {
        return null;
    }
}