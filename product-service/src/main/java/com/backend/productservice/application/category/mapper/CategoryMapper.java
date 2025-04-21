package com.backend.productservice.application.category.mapper;

import com.backend.productservice.application.category.dto.CategoryResponse;
import com.backend.productservice.application.category.dto.ProductCategoryDTO;
import com.backend.productservice.application.category.dto.ProductCategoryResponse;
import com.backend.productservice.domain.category.model.Category;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryMapper {

    public static CategoryResponse toResponse(Category category) {
        List<CategoryResponse> subCategoryResponses = category.getSubCategories()
                .stream()
                .map(CategoryMapper::toResponse)
                .toList();

        return new CategoryResponse(category.getId(), category.getName(), subCategoryResponses);
    }
    public static ProductCategoryResponse toProductResponse(Category category) {
        ProductCategoryDTO parentDTO = null;

        if (category.getParentCategory() != null) {
            Category parent = category.getParentCategory();
            parentDTO = new ProductCategoryDTO(parent.getId(), parent.getName());
        }

        return new ProductCategoryResponse(category.getId(), category.getName(), parentDTO);
    }
}