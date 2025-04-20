package com.backend.productservice.infrastructure.persistence.product;

import com.backend.productservice.domain.product.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SpringDataJpaProductRepository extends JpaRepository<Product, Long> {
    @Query("""
    SELECT DISTINCT p FROM Product p
    LEFT JOIN p.category c
    LEFT JOIN p.tags t
    WHERE (:category IS NULL OR LOWER(CAST(c.name AS string)) = LOWER(CAST(:category AS string)))
    AND (:tags IS NULL OR t.name IN :tags)
""")
    Page<Product> findFiltered(String category, List<String> tags, Pageable pageable);
}
