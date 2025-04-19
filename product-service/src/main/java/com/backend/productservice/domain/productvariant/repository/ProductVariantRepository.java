package com.backend.productservice.domain.productvariant.repository;

import com.backend.productservice.domain.productvariant.model.ProductVariant;

public interface ProductVariantRepository {
    ProductVariant save(ProductVariant productVariant);
}
