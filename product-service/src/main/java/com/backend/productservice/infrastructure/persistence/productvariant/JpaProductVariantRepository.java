package com.backend.productservice.infrastructure.persistence.productvariant;

import com.backend.productservice.domain.productvariant.model.ProductVariant;
import com.backend.productservice.domain.productvariant.repository.ProductVariantRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaProductVariantRepository implements ProductVariantRepository {
    private final SpringDataJpaProductVariantRepository jpaRepo;

    public JpaProductVariantRepository(SpringDataJpaProductVariantRepository jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public ProductVariant save(ProductVariant productVariant) {
        return jpaRepo.save(productVariant);
    }
    @Override
    public Optional<ProductVariant> findBySku(String sku) {
        return jpaRepo.findBySku(sku);
    }
}