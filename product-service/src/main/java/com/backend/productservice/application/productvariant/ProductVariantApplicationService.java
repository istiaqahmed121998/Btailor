package com.backend.productservice.application.productvariant;

import com.backend.productservice.application.productvariant.dto.ProductVariantDTO;
import com.backend.productservice.application.productvariant.mapper.ProductVariantMapper;
import com.backend.productservice.domain.productvariant.model.ProductVariant;
import com.backend.productservice.domain.productvariant.repository.ProductVariantRepository;
import jakarta.validation.Valid;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProductVariantApplicationService {
    private final ProductVariantRepository productVariantRepository;

    public ProductVariantApplicationService(ProductVariantRepository productVariantRepository) {
        this.productVariantRepository = productVariantRepository;
    }

    public ProductVariant createProductVariant(ProductVariantDTO variantDTO) {
        ProductVariant productVariant=ProductVariantMapper.toProductVariant(variantDTO);
        return productVariantRepository.save(productVariant);
    }
    public Set<ProductVariant> createProductVariants(List<@Valid ProductVariantDTO> variants) {
        Set<ProductVariant> productVariantList= new HashSet<>();
        for (ProductVariantDTO variantDTO : variants) {
            productVariantList.add(createProductVariant(variantDTO));
        }
        return productVariantList;
    }
}
