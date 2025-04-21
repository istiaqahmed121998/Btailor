package com.backend.productservice.application.productvariant.dto;

import jakarta.validation.constraints.*;

public record ProductVariantRequest(
        @NotBlank(message = "Variant size is required")
        String size,

        @NotBlank(message = "Variant color is required")
        String color,

        Double price,

        @NotNull(message = "Variant stock is required")
        @Min(value = 0, message = "Stock cannot be negative")
        Integer stock


) {
}
