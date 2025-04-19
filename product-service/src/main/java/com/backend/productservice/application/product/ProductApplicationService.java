package com.backend.productservice.application.product;

import com.backend.productservice.application.product.dto.ProductDTO;
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
import java.math.BigDecimal;
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

    public Product createProduct(ProductDTO productDto,Long sellerId) throws IOException {
        Product product = ProductMapper.toEntity(productDto);
        categoryRepository.findById(productDto.categoryId()).ifPresent(product::setCategory);
        Set<Tag> tags=new HashSet<>();
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
        productVariantApplicationService.createProductVariants(productDto.variants()).forEach(product::addVariant);

        product.setSellerId(sellerId);

        return productRepository.save(product);
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }




    public Page<Product> getFilteredProducts(String category, List<String> tags, BigDecimal priceMin, BigDecimal priceMax, Pageable pageable) {
        return productRepository.findFiltered(category, tags, priceMin, priceMax, pageable);
    }
}
