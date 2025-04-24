package com.backend.productservice.application.product.mapper;

import com.backend.productservice.application.product.dto.ProductSnapshotDto;
import com.backend.productservice.domain.product.model.Product;
import com.backend.productservice.domain.productvariant.model.ProductVariant;

public class ProductSnapshotMapper {
    public static ProductSnapshotDto toSnapshot(Product product, ProductVariant productVariant) {
        return new ProductSnapshotDto(product.getName(), productVariant.getColor(), productVariant.getSize(), product.getThumbnailUrl(), productVariant.getPrice());
    }
}
