package com.backend.productservice.application.product.dto;

import com.backend.productservice.application.category.dto.ProductCategoryResponse;
import com.backend.productservice.application.productimage.dto.ProductImageDTO;
import com.backend.productservice.application.productvariant.dto.ProductVariantResponse;

import java.util.List;
import java.util.Set;

public record ProductResponse(String name, String description, ProductCategoryResponse productCategoryResponse , Set<String> tags, List<ProductVariantResponse> variantResponses, List<ProductImageDTO> imageDTOS) {}

