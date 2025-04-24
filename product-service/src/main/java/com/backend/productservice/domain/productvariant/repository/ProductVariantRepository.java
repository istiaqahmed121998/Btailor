package com.backend.productservice.domain.productvariant.repository;

import com.backend.productservice.domain.productvariant.model.ProductVariant;

import java.util.Optional;

public interface ProductVariantRepository {
    ProductVariant save(ProductVariant productVariant);
    Optional<ProductVariant> findBySku(String sku);
}
