package com.backend.cartservice.infrastructure;

import com.backend.common.dto.CartItem;
import org.springframework.data.redis.core.ReactiveListOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Repository
public class RedisCartRepository {

    private final ReactiveRedisTemplate<String, CartItem> redisTemplate;
    private final ReactiveListOperations<String, CartItem> listOps;

    public RedisCartRepository(ReactiveRedisTemplate<String, CartItem> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.listOps = redisTemplate.opsForList();
    }

    private String key(Long userId) {
        return "cart:" + userId;
    }

    public Mono<Long> getCartSize(Long userId) {
        return listOps.size(key(userId));
    }

    public Mono<Void> addItem(Long userId, CartItem item, Duration ttl) {
        String redisKey = key(userId);
        return listOps.rightPush(redisKey, item)
                .then(redisTemplate.expire(redisKey, ttl))
                .then();
    }

    public Flux<CartItem> getCartItems(Long userId) {
        return listOps.range(key(userId), 0, -1);
    }

    public Mono<Void> clearCart(Long userId) {
        return redisTemplate.delete(key(userId)).then();
    }
}
