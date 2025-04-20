package com.backend.productservice.infrastructure.sku;

import com.backend.productservice.domain.product.service.SkuGenerator;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Random;

@Component
public class DefaultSkuGenerator implements SkuGenerator {

    private static final Random RANDOM = new Random();

    @Override
    public String generate(String sellerCode, String productType, String color, String size) {
        String shortRandom = randomAlphaNumeric(5);
        return String.format("%s-%s-%s-%s-%s",
                normalize(sellerCode),
                normalize(productType),
                normalize(color),
                normalize(size),
                shortRandom
        ).toUpperCase(Locale.ROOT);
    }

    private String normalize(String input) {
        return input == null ? "NA" : input.replaceAll("[^a-zA-Z0-9]", "").toUpperCase(Locale.ROOT);
    }

    private String randomAlphaNumeric(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(RANDOM.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
