package com.backend.inventoryservice.infrastructure.persistance;

import com.backend.inventoryservice.application.model.InventoryItem;
import com.backend.inventoryservice.application.repository.InventoryRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaInventoryRepository implements InventoryRepository {

    private final SpringDataJpaInventoryRepository jpaRepo;

    public JpaInventoryRepository(SpringDataJpaInventoryRepository jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public Optional<InventoryItem> findByVariantSku(String variantSku) {
        return jpaRepo.findByVariantSku(variantSku);
    }


    @Override
    public InventoryItem save(InventoryItem item) {
        return jpaRepo.save(item);
    }
}
