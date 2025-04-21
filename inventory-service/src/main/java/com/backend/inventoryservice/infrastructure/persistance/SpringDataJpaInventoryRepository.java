package com.backend.inventoryservice.infrastructure.persistance;

import com.backend.inventoryservice.domain.model.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataJpaInventoryRepository extends JpaRepository<InventoryItem, Long> {

    Optional<InventoryItem> findByVariantSku(String variantSku);
}
