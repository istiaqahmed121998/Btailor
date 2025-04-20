package com.backend.productservice.application.product.mapper;

import com.backend.productservice.application.product.dto.ProductDTO;
import com.backend.productservice.domain.product.model.Product;


public class ProductMapper {
    public static Product toEntity(ProductDTO dto) {
        Product p = new Product();
        p.setName(dto.name());
        p.setDescription(dto.description());
        return p;
    }

    public static ProductDTO toDTO(Product p) {
        return new ProductDTO(
                p.getName(),
                p.getDescription(),
                p.getCategory().getId(),
                null,
                null, // variants
                null

        );
    }
}
