package com.backend.productservice.interfaces.rest.productimage;

import com.backend.productservice.exception.ValidationException;
import com.backend.productservice.application.productimage.ProductImageApplicationService;
import com.backend.productservice.domain.productimage.model.ProductImage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController("/api/product")
public class ProductImageController {
    private static final String INVALID_IMAGE_DATA = "Invalid image data";
    private final ProductImageApplicationService productImageApplicationService;

    public ProductImageController(ProductImageApplicationService productImageApplicationService) {
        this.productImageApplicationService = productImageApplicationService;
    }

    @PostMapping("/{id}/images")
    public ResponseEntity<?> createProductImage(@PathVariable Long id, @RequestParam("images") List<MultipartFile> images, BindingResult bindingResult) {
        for (MultipartFile image : images) {
            String filename = image.getOriginalFilename();
            if (filename != null && !filename.matches("(?i).*\\.(jpg|jpeg|png|webp)$")) {
                bindingResult.addError(new ObjectError(filename,"Invalid file extension"));
            }
        }
        if (bindingResult.hasErrors()) {
            throw new ValidationException(INVALID_IMAGE_DATA, bindingResult);
        }


        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
