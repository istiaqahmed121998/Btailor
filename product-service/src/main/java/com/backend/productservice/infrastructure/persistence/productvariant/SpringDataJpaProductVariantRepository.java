package com.backend.productservice.infrastructure.persistence.productvariant;

import com.backend.productservice.domain.productvariant.model.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataJpaProductVariantRepository extends JpaRepository<ProductVariant, Long> {
    Optional<ProductVariant> findBySku(String sku);
}
