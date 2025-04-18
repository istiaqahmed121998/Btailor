package com.backend.productservice.domain.productimage;

import com.backend.productservice.domain.product.model.Product;
import com.backend.productservice.domain.productimage.model.ProductImage;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductImageService {
    private static final int MAX_IMAGES = 6;

    public void addImagesToProduct(Product product, List<ProductImage> images) {
        if (product.getImages().size() + images.size() > MAX_IMAGES) {
            throw new IllegalArgumentException("Exceeded max number of product images");
        }

        for (ProductImage image : images) {
            product.addImage(image);
        }
    }

}
