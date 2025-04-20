package com.backend.productservice.application.productvariant;


import com.backend.productservice.domain.productvariant.model.ProductVariant;
import com.backend.productservice.domain.productvariant.repository.ProductVariantRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Service
public class ProductVariantApplicationService {
    private final ProductVariantRepository productVariantRepository;
    public ProductVariantApplicationService(ProductVariantRepository productVariantRepository) {
        this.productVariantRepository = productVariantRepository;
    }

    public ProductVariant createProductVariant(ProductVariant productVariant) {
        return productVariantRepository.save(productVariant);
    }
    public Set<ProductVariant> createProductVariants(List<@Valid ProductVariant> variants) {
        Set<ProductVariant> productVariantList= new HashSet<>();
        for (ProductVariant variant : variants) {
            productVariantList.add(createProductVariant(variant));
        }
        return productVariantList;
    }
}
