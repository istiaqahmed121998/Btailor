package com.backend.productservice.interfaces.rest.productvariant;

import com.backend.productservice.application.product.dto.ProductResponse;
import com.backend.productservice.application.product.mapper.ProductMapper;
import com.backend.productservice.application.productvariant.ProductVariantApplicationService;
import com.backend.productservice.application.productvariant.dto.ProductVariantRequest;
import com.backend.productservice.domain.productvariant.model.ProductVariant;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products/{productId}/variants")
public class ProductVariantController {

    private final ProductVariantApplicationService productVariantService;

    public ProductVariantController(ProductVariantApplicationService productVariantService) {
        this.productVariantService = productVariantService;
    }
    @PostMapping
    public ResponseEntity<ProductResponse> addVariantToProduct(
            @PathVariable Long productId,
            @RequestBody ProductVariantRequest variantRequest
    ) {
        ProductVariant productVariant = productVariantService.addVariantToProduct(productId, variantRequest);
        return ResponseEntity.ok(ProductMapper.toProductResponse(productVariant.getProduct()));
    }
}
