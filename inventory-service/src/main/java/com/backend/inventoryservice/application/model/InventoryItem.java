package com.backend.inventoryservice.application.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_items")
public class InventoryItem {

    @Id
    @GeneratedValue
    private Long id;

    private Long productId;
    private Long variantId;

    private Integer availableQuantity;
    private Integer reservedQuantity;

    private LocalDateTime lastUpdated;

    public InventoryItem() {

    }

    public InventoryItem(Long productId, Long variantId, Long id, Integer reservedQuantity, Integer availableQuantity, LocalDateTime lastUpdated) {
        this.productId = productId;
        this.variantId = variantId;
        this.id = id;
        this.reservedQuantity = reservedQuantity;
        this.availableQuantity = availableQuantity;
        this.lastUpdated = lastUpdated;
    }

    public boolean reserve(int quantity) {
        if (availableQuantity >= quantity) {
            availableQuantity -= quantity;
            reservedQuantity += quantity;
            return true;
        }
        return false;
    }

    public void release(int quantity) {
        reservedQuantity -= quantity;
        availableQuantity += quantity;
    }

    public void deduct(int quantity) {
        reservedQuantity -= quantity;
    }



}

