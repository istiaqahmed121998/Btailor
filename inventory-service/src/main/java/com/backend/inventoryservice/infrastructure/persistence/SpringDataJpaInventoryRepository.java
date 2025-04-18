package com.backend.inventoryservice.infrastructure.persistence;

import com.backend.inventoryservice.application.model.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataJpaInventoryRepository extends JpaRepository<InventoryItem, Long> {

}
