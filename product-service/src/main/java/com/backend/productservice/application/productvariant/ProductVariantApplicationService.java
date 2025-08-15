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
import com.backend.productservice.exception.ServiceUnavailableException;
import com.backend.productservice.infrastructure.feign.InventoryClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(propagation = Propagation.REQUIRED)
//    @CircuitBreaker(name = "inventory-service", fallbackMethod = "getInventoryFallback")
    public ProductVariant createProductVariant(Product product, ProductVariantRequest productVariantRequest) {
        ProductVariant productVariant=ProductVariantMapper.toProductVariant(productVariantRequest);
        String sku=skuGenerator.generate(
                String.valueOf(product.getSellerId()),
                product.getCategory().getName(),
                productVariant.getColor(), productVariant.getSize());
        productVariant.setSku(sku);
        productVariant.setProduct(product);
        ProductVariant savedProductVariant = productVariantRepository.save(productVariant);
//        try {
//    } catch (Exception e) {
//            // Log and throw transaction-aware exception
//            throw new ServiceUnavailableException("Inventory service unavailable - transaction rolled back"+e.getMessage());
//        }
        inventoryClient.initializeInventory(savedProductVariant.getSku(), productVariantRequest.stock());
        return savedProductVariant;
    }

    @Transactional
    public ProductVariant addVariantToProduct(Long productId, ProductVariantRequest variantRequest) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id " + productId + " not found"));

        return createProductVariant(product, variantRequest);
    }
    @Transactional(readOnly = true)
    public ProductSnapshotDto getProductVariant(String variantSku) {
        ProductVariant variant =productVariantRepository.findBySku(variantSku).orElseThrow(() -> new ResourceNotFoundException("Product Variant is not found"));
        return ProductSnapshotMapper.toSnapshot(variant.getProduct(), variant);
    }
    // The fallback that gets executed when the circuit is OPEN.
    public ProductVariant getInventoryFallback(Product product, ProductVariantRequest productVariantRequest, Throwable t) {
        // Log the error properly (see recommendation #3)
        System.out.println("Fallback triggered for creating variant for product ID: {}. Reason: {}"+ product.getId()+ t.getMessage());

        // Since we can't create the variant, we must throw an exception
        // to roll back the transaction (see issue #2).
        throw new ServiceUnavailableException("Inventory service is currently unavailable. The product variant could not be created.");
    }
}
