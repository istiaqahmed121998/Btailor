package com.backend.productservice.application.productvariant.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record ProductVariantDTO(

        @NotBlank(message = "Variant size is required")
        String size,

        @NotBlank(message = "Variant color is required")
        String color,

        @NotNull(message = "Variant price is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Variant price must be greater than 0")
        BigDecimal price,

        @NotNull(message = "Variant stock is required")
        @Min(value = 0, message = "Stock cannot be negative")
        Integer stock

) {}
