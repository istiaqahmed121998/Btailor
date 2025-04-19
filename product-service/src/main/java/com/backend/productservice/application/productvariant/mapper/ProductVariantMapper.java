package com.backend.productservice.application.productvariant.mapper;

import com.backend.productservice.application.productvariant.dto.ProductVariantDTO;
import com.backend.productservice.domain.productvariant.model.ProductVariant;

public class ProductVariantMapper {
    private ProductVariantMapper() {
        throw new IllegalStateException("ProductVariant class");
    }
    public static ProductVariantDTO toProductVariantDTO(ProductVariant productVariant) {
        return new ProductVariantDTO(productVariant.getSize(), productVariant.getColor(), productVariant.getPrice(),productVariant.getStock());
    }
    public static ProductVariant toProductVariant(ProductVariantDTO productVariantDTO) {
        ProductVariant productVariant = new ProductVariant();
        productVariant.setColor(productVariantDTO.color());
        productVariant.setSize(productVariantDTO.size());
        productVariant.setPrice(productVariantDTO.price());
        productVariant.setStock(productVariantDTO.stock());
        return productVariant;
    }
}
