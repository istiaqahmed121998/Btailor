package com.backend.cartservice.application;

import com.backend.cartservice.domain.model.CartItem;
import com.backend.cartservice.exception.CartEmptyException;
import com.backend.cartservice.exception.CartFullException;
import com.backend.cartservice.exception.OutOfStockException;
import com.backend.cartservice.infrastructure.InventoryClient;
import com.backend.cartservice.infrastructure.RedisCartRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
@Service
public class CartService{
    private static final int MAX_CART_ITEMS = 50;
    private static final Duration CART_TTL = Duration.ofHours(24);

    private final RedisCartRepository redisRepository;
    private final InventoryClient inventoryClient;
    private final CheckoutPublisher checkoutPublisher;

    public CartService(RedisCartRepository redisRepository,
                       InventoryClient inventoryClient,
                       CheckoutPublisher checkoutPublisher) {
        this.redisRepository = redisRepository;
        this.inventoryClient = inventoryClient;
        this.checkoutPublisher = checkoutPublisher;
    }

    public Mono<Void> addItem(Long userId, CartItem item) {
        return redisRepository.getCartSize(userId)
                .flatMap(size -> {
                    if (size >= MAX_CART_ITEMS) return Mono.error(new CartFullException());
                    if (!inventoryClient.isAvailable(item.skuVariant(), item.quantity())) {
                        return Mono.error(new OutOfStockException());
                    }
                    return redisRepository.addItem(userId, item, CART_TTL);
                });
    }

    public Flux<CartItem> getCart(Long userId) {
        return redisRepository.getCartItems(userId);
    }

    public Mono<Void> clearCart(Long userId) {
        return redisRepository.clearCart(userId);
    }

    public Mono<Void> checkout(Long userId, Long shippingAddressId, String paymentMethod) {
        return redisRepository.getCartItems(userId).collectList()
                .flatMap(items -> {
                    if (items.isEmpty()) return Mono.error(new CartEmptyException());
                    checkoutPublisher.publish(userId, shippingAddressId, paymentMethod, items);
                    return redisRepository.clearCart(userId);
                });
    }
}
