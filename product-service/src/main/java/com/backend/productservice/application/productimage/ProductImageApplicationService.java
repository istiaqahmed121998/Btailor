package com.backend.productservice.application.productimage;

import com.backend.productservice.domain.product.repository.ProductRepository;
import com.backend.productservice.domain.productimage.model.ProductImage;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class ProductImageApplicationService {
    private final ProductRepository productRepository;

    public ProductImageApplicationService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductImage createProductImage(Long id, List<MultipartFile> images) {
//        productRepository.findById(id).ifPresentOrElse(product -> {
//            product.addImage();
//        });
        return null;
    }
}
