package com.backend.inventoryservice.domain.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "inventory_items",
        uniqueConstraints = @UniqueConstraint(columnNames = "variant_sku")
)
public class InventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "variant_sku", nullable = false, unique = true)
    private String variantSku;

    @Column(name = "available_quantity", nullable = false)
    private Integer availableQuantity;

    @Column(name = "reserved_quantity", nullable = false)
    private Integer reservedQuantity;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    protected InventoryItem() {}

    public InventoryItem(String variantSku, int availableQuantity) {
        this.variantSku = variantSku;
        this.availableQuantity = availableQuantity;
        this.reservedQuantity = 0;
        this.lastUpdated = LocalDateTime.now();
    }

    public boolean reserve(int quantity) {
        if (availableQuantity >= quantity) {
            availableQuantity -= quantity;
            reservedQuantity += quantity;
            this.lastUpdated = LocalDateTime.now();
            return true;
        }
        return false;
    }

    public void release(int quantity) {
        reservedQuantity -= quantity;
        availableQuantity += quantity;
        this.lastUpdated = LocalDateTime.now();
    }

    public void deduct(int quantity) {
        if (reservedQuantity < quantity) {
            throw new IllegalStateException("Cannot deduct more than reserved quantity");
        }
        reservedQuantity -= quantity;
        this.lastUpdated = LocalDateTime.now();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public String getVariantSku() {
        return variantSku;
    }

    public void setVariantSku(String variantSku) {
        this.variantSku = variantSku;
    }

    public Integer getReservedQuantity() {
        return reservedQuantity;
    }

    public void setReservedQuantity(Integer reservedQuantity) {
        this.reservedQuantity = reservedQuantity;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}