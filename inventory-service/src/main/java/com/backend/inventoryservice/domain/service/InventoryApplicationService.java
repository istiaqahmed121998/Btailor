package com.backend.inventoryservice.domain.service;

import com.backend.inventoryservice.application.model.InventoryItem;
import com.backend.inventoryservice.application.repository.InventoryRepository;
import jakarta.transaction.Transactional;

public class InventoryApplicationService {
    private final InventoryRepository repository;

    public InventoryApplicationService(InventoryRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public boolean reserveStock(Long productId, Long variantId, int quantity) {
        InventoryItem item = repository.findByProductAndVariant(productId, variantId)
                .orElseThrow(() -> new RuntimeException("Not found"));

        boolean reserved = item.reserve(quantity);
        if (reserved) {
            repository.save(item);
        }
        return reserved;
    }

    @Transactional
    public void releaseStock(Long productId, Long variantId, int quantity) {
        InventoryItem item = repository.findByProductAndVariant(productId, variantId)
                .orElseThrow();
        item.release(quantity);
        repository.save(item);
    }

    @Transactional
    public void deductStock(Long productId, Long variantId, int quantity) {
        InventoryItem item = repository.findByProductAndVariant(productId, variantId)
                .orElseThrow();
        item.deduct(quantity);
        repository.save(item);
    }
}
