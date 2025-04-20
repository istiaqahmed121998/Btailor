package com.backend.productservice.application.product;

import com.backend.productservice.application.product.dto.ProductDTO;
import com.backend.productservice.application.product.mapper.ProductMapper;
import com.backend.productservice.application.productvariant.ProductVariantApplicationService;
import com.backend.productservice.application.productvariant.mapper.ProductVariantMapper;
import com.backend.productservice.common.service.ImageStorageService;
import com.backend.productservice.domain.Tag.model.Tag;
import com.backend.productservice.domain.Tag.repository.TagRepository;
import com.backend.productservice.domain.category.repository.CategoryRepository;
import com.backend.productservice.domain.product.model.Product;
import com.backend.productservice.domain.product.repository.ProductRepository;
import com.backend.productservice.domain.product.service.SkuGenerator;
import com.backend.productservice.domain.productimage.model.ProductImage;
import com.backend.productservice.domain.productvariant.model.ProductVariant;
import com.backend.productservice.infrastructure.feign.InventoryClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class ProductApplicationService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final ImageStorageService imageStorageService;
    private final ProductVariantApplicationService productVariantApplicationService;
    private final InventoryClient inventoryClient;
    private final SkuGenerator skuGenerator;
    public ProductApplicationService(ProductRepository productRepository, CategoryRepository categoryRepository, TagRepository tagRepository, ImageStorageService imageStorageService, ProductVariantApplicationService productVariantApplicationService, InventoryClient inventoryClient, SkuGenerator skuGenerator) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
        this.imageStorageService = imageStorageService;
        this.productVariantApplicationService = productVariantApplicationService;
        this.inventoryClient = inventoryClient;
        this.skuGenerator = skuGenerator;
    }

    public Product createProduct(ProductDTO productDto,Long sellerId) throws IOException {
        Product product = ProductMapper.toEntity(productDto);
        categoryRepository.findById(productDto.categoryId()).ifPresent(product::setCategory);
        Set<Tag> tags=new HashSet<>();
        product.setSellerId(sellerId);
        productDto.tags().forEach(tag -> {
            tagRepository.findByName(tag).ifPresentOrElse(tags::add, () -> {
                Tag newTag=new Tag();
                newTag.setName(tag);
                tags.add(tagRepository.save(newTag));
            });
        });
        product.setTags(tags);
        imageStorageService.uploadImages(productDto.images()).forEach(imageLink -> {
            ProductImage productImage=new ProductImage();
            productImage.setImageUrl(imageLink);
            product.addImage(productImage);
        });

        Product savedProduct=productRepository.save(product);

        productDto.variants().forEach(productVariantDTO -> {
            ProductVariant productVariant=ProductVariantMapper.toProductVariant(productVariantDTO);
            String sku=skuGenerator.generate(
                    String.valueOf(sellerId),
                    product.getCategory().getName(),
                    productVariantDTO.color(), productVariantDTO.size());
            productVariant.setSku(sku);
            productVariant.setProduct(savedProduct);
            productVariantApplicationService.createProductVariant(productVariant);
            inventoryClient.initializeInventory(sku,productVariantDTO.stock());
        });


       return savedProduct;
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Page<Product> getFilteredProducts(String category, List<String> tags, Pageable pageable) {
        return productRepository.findFiltered(category, tags, pageable);
    }
}
