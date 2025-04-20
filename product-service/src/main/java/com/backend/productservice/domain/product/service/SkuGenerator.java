package com.backend.productservice.domain.product.service;

public interface SkuGenerator {
    String generate(String sellerCode, String productType, String color, String size);
}
