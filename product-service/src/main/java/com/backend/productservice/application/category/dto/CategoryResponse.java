package com.backend.productservice.application.category.dto;

import java.util.List;

public record CategoryResponse(Long id, String name, List<CategoryResponse> subCategories) {
}
