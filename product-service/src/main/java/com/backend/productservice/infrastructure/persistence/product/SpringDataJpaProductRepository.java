package com.backend.productservice.infrastructure.persistence.product;

import com.backend.productservice.domain.product.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface SpringDataJpaProductRepository extends JpaRepository<Product, Long> {
    @Query("""
    SELECT DISTINCT p FROM Product p
    LEFT JOIN p.category c
    LEFT JOIN p.tags t
    WHERE (:category IS NULL OR LOWER(CAST(c.name AS string)) = LOWER(CAST(:category AS string)))
    AND (:tags IS NULL OR t.name IN :tags)
    AND (:priceMin IS NULL OR p.price >= :priceMin)
    AND (:priceMax IS NULL OR p.price <= :priceMax)
""")
    Page<Product> findFiltered(String category, List<String> tags, BigDecimal priceMin, BigDecimal priceMax, Pageable pageable);
}
