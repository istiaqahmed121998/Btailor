package com.backend.productservice.interfaces.rest.productvariant;

import com.backend.productservice.application.product.ProductApplicationService;
import com.backend.productservice.application.product.dto.ProductResponse;
import com.backend.productservice.application.product.dto.ProductSnapshotDto;
import com.backend.productservice.application.product.mapper.ProductMapper;
import com.backend.productservice.application.productvariant.ProductVariantApplicationService;
import com.backend.productservice.application.productvariant.dto.ProductVariantRequest;
import com.backend.common.security.annotation.CurrentUserId;
import com.backend.productservice.domain.productvariant.model.ProductVariant;
import com.backend.productservice.exception.CustomUnauthorizedException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductVariantController {

    private final ProductVariantApplicationService productVariantService;
    private final ProductApplicationService productApplicationService;

    public ProductVariantController(ProductVariantApplicationService productVariantService, ProductApplicationService productApplicationService) {
        this.productVariantService = productVariantService;
        this.productApplicationService = productApplicationService;
    }
    @PreAuthorize("hasAnyRole('ADMIN','VENDOR')")
    @PostMapping("/{productId}/variants")
    public ResponseEntity<ProductResponse> addVariantToProduct(@CurrentUserId Long loggedInUserId,
            @PathVariable Long productId,
            @RequestBody ProductVariantRequest variantRequest
    ) {
        // Check product ownership
        if (!productApplicationService.isOwner(productId, loggedInUserId)) {
            throw new CustomUnauthorizedException("You do not have permission to add a variant to a product");
        }
        ProductVariant productVariant = productVariantService.addVariantToProduct(productId, variantRequest);
        return ResponseEntity.ok(ProductMapper.toProductResponse(productVariant.getProduct()));
    }
    @GetMapping("/variants/{variantSku}")
    public ResponseEntity<ProductSnapshotDto> getProductVariant(@PathVariable String variantSku) {
        ProductSnapshotDto productSnapshotDto = productVariantService.getProductVariant(variantSku);
        return ResponseEntity.ok(productSnapshotDto);
    }
}
