package com.backend.productservice.infrastructure.persistence.productvariant;

import com.backend.productservice.domain.productvariant.model.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataJpaProductVariantRepository extends JpaRepository<ProductVariant, Long> {
}
