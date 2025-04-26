package com.backend.cartservice.interfaces.rest;

import com.backend.cartservice.application.ApplicationCartService;
import com.backend.cartservice.application.dto.CheckoutRequest;
import com.backend.common.dto.CartItem;
import com.backend.common.security.annotation.CurrentUserId;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final ApplicationCartService applicationCartService;

    public CartController(ApplicationCartService applicationCartService) {
        this.applicationCartService = applicationCartService;
    }

    @PostMapping("/")
    public Mono<ResponseEntity<Void>> addItem(@AuthenticationPrincipal Jwt jwt, @CurrentUserId Long userId, @RequestBody CartItem item) {
        return applicationCartService.addItem(jwt,userId, item).thenReturn(ResponseEntity.ok().build());
    }

    @GetMapping("/")
    public Flux<CartItem> getCart(@CurrentUserId Long userId) {
        return applicationCartService.getCart(userId);
    }

    @DeleteMapping("/")
    public Mono<ResponseEntity<Void>> clearCart(@CurrentUserId Long userId) {
        return applicationCartService.clearCart(userId).thenReturn(ResponseEntity.noContent().build());
    }

    @PostMapping("/checkout")
    public Mono<ResponseEntity<Void>> checkout(@CurrentUserId Long userId, @RequestBody CheckoutRequest checkoutRequest) {
        return applicationCartService.checkout(userId, checkoutRequest.shippingAddress(), checkoutRequest.paymentMethod())
                .thenReturn(ResponseEntity.accepted().build());
    }
}