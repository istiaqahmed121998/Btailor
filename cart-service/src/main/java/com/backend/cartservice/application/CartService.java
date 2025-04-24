package com.backend.cartservice.application;

import com.backend.cartservice.exception.CartEmptyException;
import com.backend.cartservice.exception.CartFullException;
import com.backend.cartservice.exception.OutOfStockException;
import com.backend.cartservice.infrastructure.InventoryClient;
import com.backend.cartservice.infrastructure.ProductWebClient;
import com.backend.cartservice.infrastructure.RedisCartRepository;
import com.backend.common.dto.CartItem;
import com.backend.common.events.CartCheckedOutEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
public class CartService{
    private static final int MAX_CART_ITEMS = 50;
    private static final Duration CART_TTL = Duration.ofHours(24);

    private final RedisCartRepository redisRepository;
    private final InventoryClient inventoryClient;
    private final CheckoutPublisher checkoutPublisher;
    private final ProductWebClient productWebClient;

    public CartService(RedisCartRepository redisRepository,
                       InventoryClient inventoryClient,
                       CheckoutPublisher checkoutPublisher, ProductWebClient productWebClient) {
        this.redisRepository = redisRepository;
        this.inventoryClient = inventoryClient;
        this.checkoutPublisher = checkoutPublisher;
        this.productWebClient = productWebClient;
    }

    public Mono<Void> addItem(Long userId, CartItem item) {
        return redisRepository.getCartSize(userId)
                .flatMap(size -> {
                    if (size >= MAX_CART_ITEMS) return Mono.error(new CartFullException());
                    if (!inventoryClient.isAvailable(item.getVariantSku(), item.getQuantity())) {
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

    public Mono<Void> checkout(Long userId, String shippingAddress, String paymentMethod) {
        return redisRepository.getCartItems(userId).collectList()
                .flatMap(items -> {
                    if (items.isEmpty()) return Mono.error(new CartEmptyException());

                    List<Mono<CartItem>> itemMonos = items.stream().map(item ->
                            productWebClient.getSnapshot(item.getVariantSku())
                                    .map(snapshot -> {
                                        CartItem eventItem = new CartItem();
                                        eventItem.setVariantSku(item.getVariantSku());
                                        eventItem.setQuantity(item.getQuantity());
                                        eventItem.setPrice(snapshot.getPrice());
                                        eventItem.setProductName(snapshot.getProductName());
                                        eventItem.setColor(snapshot.getColor());
                                        eventItem.setSize(snapshot.getSize());
                                        eventItem.setThumbnail(snapshot.getThumbnail());
                                        return eventItem;
                                    })
                    ).toList();
                    return Flux.merge(itemMonos).collectList()
                            .flatMap(eventItems -> {
                                CartCheckedOutEvent event = new CartCheckedOutEvent(
                                        userId,
                                        eventItems,
                                        paymentMethod,
                                        shippingAddress,
                                        Instant.now()
                                );
                                checkoutPublisher.publish(event);
                                return redisRepository.clearCart(userId);
                            });
                });
    }
}
