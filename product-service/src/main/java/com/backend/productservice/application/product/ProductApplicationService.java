package com.backend.productservice.application.product;

import com.backend.productservice.application.product.dto.ProductRequest;
import com.backend.productservice.application.product.dto.ProductResponse;
import com.backend.productservice.application.product.mapper.ProductMapper;
import com.backend.productservice.application.productvariant.ProductVariantApplicationService;
import com.backend.productservice.common.service.ImageStorageService;
import com.backend.productservice.domain.Tag.model.Tag;
import com.backend.productservice.domain.Tag.repository.TagRepository;
import com.backend.productservice.domain.category.repository.CategoryRepository;
import com.backend.productservice.domain.product.model.Product;
import com.backend.productservice.domain.product.repository.ProductRepository;
import com.backend.productservice.domain.productimage.model.ProductImage;
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

    public ProductApplicationService(ProductRepository productRepository, CategoryRepository categoryRepository, TagRepository tagRepository, ImageStorageService imageStorageService, ProductVariantApplicationService productVariantApplicationService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
        this.imageStorageService = imageStorageService;
        this.productVariantApplicationService = productVariantApplicationService;

    }

    public Product createProduct(ProductRequest productRequest, Long sellerId) throws IOException {
        Product product = ProductMapper.toEntity(productRequest);
        categoryRepository.findById(productRequest.categoryId()).ifPresent(product::setCategory);
        Set<Tag> tags=new HashSet<>();
        product.setSellerId(sellerId);
        productRequest.tags().forEach(tag -> {
            tagRepository.findByName(tag).ifPresentOrElse(tags::add, () -> {
                Tag newTag=new Tag();
                newTag.setName(tag);
                tags.add(tagRepository.save(newTag));
            });
        });
        product.setTags(tags);
        if(product.getImages()!=null){
            imageStorageService.uploadImages(productRequest.images()).forEach(imageLink -> {
            ProductImage productImage=new ProductImage();
            productImage.setImageUrl(imageLink);
            product.addImage(productImage);
        });}


        Product savedProduct=productRepository.save(product);

        productRequest.variants().forEach(productVariantRequest -> {
            productVariantApplicationService.createProductVariant(savedProduct, productVariantRequest);

        });


       return savedProduct;
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Page<ProductResponse> getFilteredProducts(String category, List<String> tags, Pageable pageable) {
        Page<Product> products=productRepository.findFiltered(category, tags, pageable);
        return products.map(ProductMapper::toProductResponse);
    }
}
