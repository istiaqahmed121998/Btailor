package com.backend.productservice.application.product.dto;

import com.backend.productservice.application.productimage.ValidImage;
import com.backend.productservice.application.productvariant.dto.ProductVariantDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public record ProductDTO(
        @NotBlank(message = "Product name is required")
        String name,

        @NotBlank(message = "Product description is required")
        String description,

        @NotNull(message = "Product price is required")
        @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
        BigDecimal price,

        @NotNull(message = "Category ID is required")
        Long categoryId,

        @Size(max = 10, message = "A product can have up to 10 tags")
        List<@NotBlank(message = "Tag name cannot be blank") String> tags,

        List<@Valid ProductVariantDTO> variants,

        List<@ValidImage MultipartFile> images

) {}
