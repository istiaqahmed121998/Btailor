package com.backend.productservice.Product;


import com.backend.productservice.Category.Category;
import com.backend.productservice.Category.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public List<Product> getProductsByTag(String tag) {
        return productRepository.findByTag(tag);
    }

    public Product createProduct(ProductDTO dto) {
        Category category = null;
        if (dto.getCategoryId() != null) {
            category = categoryRepository.findById(dto.getCategoryId()).orElse(null);
        }
        Product product = new Product(
                dto.getName(),
                dto.getPrice(),
                dto.getDescription(),
                dto.getTag(),
                category
        );
        return productRepository.save(product);
    }
}

