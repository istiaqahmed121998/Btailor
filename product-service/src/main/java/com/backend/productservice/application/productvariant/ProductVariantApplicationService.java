package com.backend.productservice.application.productvariant;


import com.backend.productservice.application.product.dto.ProductSnapshotDto;
import com.backend.productservice.application.product.mapper.ProductSnapshotMapper;
import com.backend.productservice.exception.ResourceNotFoundException;
import com.backend.productservice.application.productvariant.dto.ProductVariantRequest;
import com.backend.productservice.application.productvariant.mapper.ProductVariantMapper;
import com.backend.productservice.domain.product.model.Product;
import com.backend.productservice.domain.product.repository.ProductRepository;
import com.backend.productservice.domain.product.service.SkuGenerator;
import com.backend.productservice.domain.productvariant.model.ProductVariant;
import com.backend.productservice.domain.productvariant.repository.ProductVariantRepository;
import com.backend.productservice.infrastructure.feign.InventoryClient;
import org.springframework.stereotype.Service;

@Service
public class ProductVariantApplicationService {
    private final ProductVariantRepository productVariantRepository;
    private final ProductRepository productRepository;
    private final SkuGenerator skuGenerator;
    private final InventoryClient inventoryClient;
    public ProductVariantApplicationService(ProductVariantRepository productVariantRepository, ProductRepository productRepository, SkuGenerator skuGenerator, InventoryClient inventoryClient) {
        this.productVariantRepository = productVariantRepository;
        this.productRepository = productRepository;
        this.skuGenerator = skuGenerator;
        this.inventoryClient = inventoryClient;
    }

    public ProductVariant createProductVariant(Product product, ProductVariantRequest productVariantRequest) {
        ProductVariant productVariant=ProductVariantMapper.toProductVariant(productVariantRequest);
        String sku=skuGenerator.generate(
                String.valueOf(product.getSellerId()),
                product.getCategory().getName(),
                productVariant.getColor(), productVariant.getSize());
        productVariant.setSku(sku);
        productVariant.setProduct(product);
        ProductVariant savedProductVariant = productVariantRepository.save(productVariant);
        inventoryClient.initializeInventory(savedProductVariant.getSku(),productVariantRequest.stock());
        return productVariantRepository.save(productVariant);
    }


    public ProductVariant addVariantToProduct(Long productId, ProductVariantRequest variantRequest) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + productId + " not found"));

        return createProductVariant(product, variantRequest);
    }

    public ProductSnapshotDto getProductVariant(String variantSku) {
        ProductVariant variant =productVariantRepository.findBySku(variantSku).orElseThrow(() -> new ResourceNotFoundException("Product Variant is not found"));
        return ProductSnapshotMapper.toSnapshot(variant.getProduct(), variant);
    }
}
