package com.backend.inventoryservice.application;

import com.backend.inventoryservice.domain.model.InventoryItem;
import com.backend.inventoryservice.domain.repository.InventoryRepository;
import com.backend.inventoryservice.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InventoryApplicationService {

    private final InventoryRepository repository;

    public InventoryApplicationService(InventoryRepository repository) {
        this.repository = repository;
    }

    /**
     * Tries to reserve the specified quantity of stock.
     * Returns true if successful, false otherwise.
     */
    @Transactional
    public void reserveStock(String variantSku, int quantity) {
        InventoryItem item = repository.findByVariantSku(variantSku)
                .orElseThrow(() -> new IllegalArgumentException("Variant SKU not found: " + variantSku));

        boolean reserved = item.reserve(quantity);
        if (reserved) {
            repository.save(item); // save only if mutated
        }
    }

    /**
     * Releases previously reserved stock.
     */
    @Transactional
    public void releaseStock(String variantSku, int quantity) {
        InventoryItem item = repository.findByVariantSku(variantSku)
                .orElseThrow(() -> new IllegalArgumentException("Variant SKU not found: " + variantSku));

        item.release(quantity);
        repository.save(item);
    }

    /**
     * Deducts stock after order completion (from reserved).
     */
    @Transactional
    public void deductStock(String variantSku, int quantity) {
        InventoryItem item = repository.findByVariantSku(variantSku)
                .orElseThrow(() -> new IllegalArgumentException("Variant SKU not found: " + variantSku));

        item.deduct(quantity); // this should throw if reserved quantity is too low
        repository.save(item);
    }

    /**
     * Used during product creation to create inventory entry.
     */
    @Transactional
    public void initializeInventory(String variantSku, int quantity) {
        repository.findByVariantSku(variantSku).ifPresentOrElse(
                item -> {
                    throw new IllegalStateException("Inventory already exists for SKU: " + variantSku);
                },
                () -> {
                    InventoryItem newItem = new InventoryItem(variantSku, quantity);
                    repository.save(newItem);
                }
        );
    }

    /**
     * Checks if the required quantity is available.
     */
    public boolean isAvailable(String variantSku, int requiredQuantity) {
        Optional<InventoryItem> itemOpt = repository.findByVariantSku(variantSku);
        return itemOpt.map(item -> item.getAvailableQuantity() >= requiredQuantity)
                .orElse(false);
    }

    public InventoryItem getBySku(String variantSku) {
        return repository.findByVariantSku(variantSku)
                .orElseThrow(() -> new ResourceNotFoundException("Variant SKU not found: " + variantSku));}
}