package com.backend.productservice.application.productvariant.mapper;

import com.backend.productservice.application.productvariant.dto.ProductVariantRequest;
import com.backend.productservice.application.productvariant.dto.ProductVariantResponse;
import com.backend.productservice.domain.productvariant.model.ProductVariant;

public class ProductVariantMapper {
    private ProductVariantMapper() {
        throw new IllegalStateException("ProductVariant class");
    }
    public static ProductVariantResponse toProductVariantResponse(ProductVariant productVariant) {
        return new ProductVariantResponse(productVariant.getSku(),productVariant.getSize(), productVariant.getColor(), productVariant.getPrice());
    }
    public static ProductVariant toProductVariant(ProductVariantRequest productVariantRequest) {
        ProductVariant productVariant = new ProductVariant();
        productVariant.setColor(productVariantRequest.color());
        productVariant.setSize(productVariantRequest.size());
        productVariant.setPrice(productVariantRequest.price());
        return productVariant;
    }
}
