package com.backend.productservice.domain.productimage.repository;

import com.backend.productservice.domain.productimage.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage,Long> {
}
