package com.backend.productservice.application.product.dto;

import com.backend.productservice.application.productimage.ValidImage;
import com.backend.productservice.application.productvariant.dto.ProductVariantDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public record ProductDTO(
        @NotBlank(message = "Product name is required")
        String name,

        @NotBlank(message = "Product description is required")
        String description,

        @NotNull(message = "Category ID is required")
        Long categoryId,

        @Size(max = 10, message = "A product can have up to 10 tags")
        Set<@NotBlank(message = "Tag name cannot be blank") String> tags,

        List<@Valid ProductVariantDTO> variants,

        List<@ValidImage MultipartFile> images

) {}
