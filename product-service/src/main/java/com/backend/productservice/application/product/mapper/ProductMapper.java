package com.backend.productservice.application.product.mapper;

import com.backend.productservice.application.category.mapper.CategoryMapper;
import com.backend.productservice.application.product.dto.ProductRequest;
import com.backend.productservice.application.product.dto.ProductResponse;
import com.backend.productservice.application.productimage.dto.ProductImageDTO;
import com.backend.productservice.application.productvariant.dto.ProductVariantResponse;
import com.backend.productservice.domain.Tag.model.Tag;
import com.backend.productservice.domain.product.model.Product;

import java.util.stream.Collectors;


public class ProductMapper {
    public static Product toEntity(ProductRequest dto) {
        Product p = new Product();
        p.setName(dto.name());
        p.setDescription(dto.description());
        return p;
    }

    public static ProductResponse toProductResponse(Product p) {
        return new ProductResponse(
                p.getName(),
                p.getDescription(),
                CategoryMapper.toProductResponse(p.getCategory()),
                p.getTags().stream().map(Tag::getName).collect(Collectors.toSet()),
                p.getVariants().stream().map(productVariant -> new ProductVariantResponse(productVariant.getSku(),productVariant.getSize(), productVariant.getColor(), productVariant.getPrice() )).toList(),
                p.getImages().stream().map((productImage ->
                        new ProductImageDTO(productImage.getImageUrl(),null))).toList()

        );
    }
}
