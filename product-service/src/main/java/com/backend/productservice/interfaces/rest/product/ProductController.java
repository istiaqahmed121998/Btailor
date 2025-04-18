package com.backend.productservice.interfaces.rest.product;

import com.backend.productservice.Exception.ValidationException;
import com.backend.productservice.application.product.ProductApplicationService;
import com.backend.productservice.application.product.dto.ProductDTO;
import com.backend.productservice.application.product.mapper.ProductMapper;
import com.backend.productservice.common.security.annotation.CurrentUserId;
import com.backend.productservice.domain.product.model.Product;
import com.backend.productservice.utils.ApiResponse;
import com.backend.productservice.utils.PaginatedResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductApplicationService productApplicationService;

    private final String INVALID_PRODUCT_DATA="Invalid User Data";

    public ProductController(ProductApplicationService productApplicationService) {
        this.productApplicationService = productApplicationService;
    }

    @PreAuthorize("hasRole('VENDOR')")
    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<?>> createProduct(@CurrentUserId Long userId, @Valid @ModelAttribute ProductDTO dto, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(INVALID_PRODUCT_DATA, bindingResult);
        }

        Product product =productApplicationService.createProduct(dto,userId);

        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.CREATED.toString(),"Product has been created",product), HttpStatus.CREATED);
    }



    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDTO>> getProductById(@PathVariable Long id) {
        Optional<Product> product = productApplicationService.getProductById(id);
        if (product.isPresent()) {
            return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.toString(),"Product Information",ProductMapper.toDTO(product.get())), HttpStatus.OK);

        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/")
    public ResponseEntity<PaginatedResponse<ProductDTO>> getAllProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) List<String> tag,
            @RequestParam(required = false) BigDecimal priceMin,
            @RequestParam(required = false) BigDecimal priceMax,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String sort){
        Pageable pageable = PageRequest.of(page, size,Sort.by(parseSort(sort)));
        Page<Product> productPage = productApplicationService.getFilteredProducts(
                category, tag, priceMin, priceMax, pageable
        );
        List<ProductDTO> dtoList = productPage.getContent()
                .stream()
                .map((ProductMapper::toDTO))
                .toList();
        return ResponseEntity.ok(new PaginatedResponse<>(HttpStatus.OK.toString(),"",dtoList,productPage.getNumber(),
                productPage.getSize(),
                productPage.getTotalElements()
        ));
    }
    private Sort.Order parseSort(String sort) {
        String[] parts = sort.split(",");
        return new Sort.Order(Sort.Direction.fromString(parts[1]), parts[0]);
    }

}
