package com.backend.productservice.application.category.dto;

public record ProductCategoryResponse(Long id, String name, ProductCategoryDTO parentCategory)  {
}
