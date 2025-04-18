package com.backend.productservice.domain.product.repository;

import com.backend.productservice.domain.product.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Product save(Product product);
    Optional<Product> findById(Long id);
    Page<Product> findAll(Pageable pageable);
    Page<Product> findFiltered(String category, List<String> tags, BigDecimal priceMin, BigDecimal priceMax, Pageable pageable);
}