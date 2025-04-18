package com.backend.productservice.application.productimage;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ValidImageValidator implements ConstraintValidator<ValidImage, MultipartFile> {

    private final List<String> allowedTypes = List.of("image/jpeg", "image/png", "image/webp");

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return false;
        }
        if (!allowedTypes.contains(file.getContentType())) {
            return false;
        }
        // 2MB
        long MAX_SIZE = 2 * 1024 * 1024;
        return file.getSize() <= MAX_SIZE;
    }
}
