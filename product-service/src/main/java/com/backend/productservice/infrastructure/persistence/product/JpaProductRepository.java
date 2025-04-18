package com.backend.productservice.infrastructure.persistence.product;

import com.backend.productservice.domain.product.model.Product;
import com.backend.productservice.domain.product.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaProductRepository implements ProductRepository {

    private final SpringDataJpaProductRepository jpaRepo;

    public JpaProductRepository(SpringDataJpaProductRepository jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public Product save(Product product) {
        return jpaRepo.save(product);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return jpaRepo.findById(id);
    }

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return jpaRepo.findAll(pageable);
    }

    @Override
    public Page<Product> findFiltered(String category, List<String> tags, BigDecimal priceMin, BigDecimal priceMax, Pageable pageable) {
        return jpaRepo.findFiltered(category,tags,priceMin,priceMax,pageable);
    }
}
