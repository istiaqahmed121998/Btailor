package com.backend.cartservice.interfaces.rest;

import com.backend.cartservice.application.CartService;
import com.backend.cartservice.common.security.annotation.CurrentUserId;
import com.backend.cartservice.domain.model.CartItem;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/")
    public Mono<ResponseEntity<Void>> addItem(@CurrentUserId Long userId, @RequestBody CartItem item) {
        return cartService.addItem(userId, item).thenReturn(ResponseEntity.ok().build());
    }

    @GetMapping("/")
    public Flux<CartItem> getCart(@CurrentUserId Long userId) {
        return cartService.getCart(userId);
    }

    @DeleteMapping("/")
    public Mono<ResponseEntity<Void>> clearCart(@CurrentUserId Long userId) {
        return cartService.clearCart(userId).thenReturn(ResponseEntity.noContent().build());
    }

    @PostMapping("/checkout")
    public Mono<ResponseEntity<Void>> checkout(@CurrentUserId Long userId,
                                               @RequestParam Long shippingAddressId,
                                               @RequestParam String paymentMethod) {
        return cartService.checkout(userId, shippingAddressId, paymentMethod)
                .thenReturn(ResponseEntity.accepted().build());
    }
}