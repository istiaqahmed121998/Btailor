package com.backend.cartservice.interfaces.rest;

import com.backend.cartservice.application.CartService;
import com.backend.cartservice.application.dto.CheckoutRequest;
import com.backend.common.dto.CartItem;
import com.backend.common.security.annotation.CurrentUserId;
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
    public Mono<ResponseEntity<Void>> checkout(@CurrentUserId Long userId, @RequestBody CheckoutRequest checkoutRequest) {
        return cartService.checkout(userId, checkoutRequest.shippingAddress(), checkoutRequest.paymentMethod())
                .thenReturn(ResponseEntity.accepted().build());
    }
}