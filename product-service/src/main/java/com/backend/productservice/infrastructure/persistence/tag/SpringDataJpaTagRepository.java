package com.backend.productservice.infrastructure.persistence.tag;

import com.backend.productservice.domain.Tag.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataJpaTagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);
}
