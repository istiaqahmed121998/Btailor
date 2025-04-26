package com.backend.cartservice.infrastructure;

import com.backend.cartservice.domain.model.UserInformation;
import com.backend.cartservice.exception.ProfileNotFoundException;
import com.backend.common.dto.CartItem;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveListOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

@Repository
public class RedisCartRepository {

    private final ReactiveRedisTemplate<String, CartItem> redisTemplate;
    private final ReactiveListOperations<String, CartItem> listOps;
    private final ReactiveRedisTemplate<String, UserInformation> profileRedisTemplate;
    private final ReactiveHashOperations<String, String, String> hashOps;

    public RedisCartRepository(ReactiveRedisTemplate<String, CartItem> redisTemplate, ReactiveRedisTemplate<String, UserInformation> profileRedisTemplate) {
        this.redisTemplate    = redisTemplate;
        this.listOps              = redisTemplate.opsForList();    // derive the list operations here
        this.profileRedisTemplate = profileRedisTemplate;
        this.hashOps              = profileRedisTemplate.opsForHash();
    }

    private String key(Long userId) {
        return "cart:" + userId;
    }
    private String profileKey(Long userId) {
        return "user:profile:" + userId;
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
    public Mono<Void> saveUserProfile(Long userId,
                                      UserInformation userInformation,
                                      Duration ttl) {
        String profileKey = "user:profile:" + userId;
        // map the record fields to a hash
        Map<String,String> profileMap = Map.of(
                "id",      userInformation.id(),
                "name",    userInformation.name(),
                "email", userInformation.email()
        );

        return hashOps
                .putAll(profileKey, profileMap)        // Mono<Boolean>
                .then(redisTemplate.expire(profileKey, ttl)) // Mono<Boolean>
                .then();                                 // Mono<Void>
    }
    public Mono<UserInformation> getUserProfile(Long userId) {
        String k = profileKey(userId);
        return hashOps.entries(k)                                // Flux<Map.Entry<String,String>>
                .collectMap(Map.Entry::getKey, Map.Entry::getValue)  // Mono<Map<String,String>>
                .flatMap(map -> {
                    if (map.isEmpty()) {
                        return Mono.error(
                                new ProfileNotFoundException("User profile not found")
                        );
                    }
                    return Mono.just(new UserInformation(
                            map.get("id"),
                            map.get("name"),
                            map.get("email")
                    ));
                });
    }

    public Flux<CartItem> getCartItems(Long userId) {
        return listOps.range(key(userId), 0, -1);
    }

    public Mono<Void> clearCart(Long userId) {
        return redisTemplate.delete(key(userId)).then(profileRedisTemplate.delete(profileKey(userId))).then();
    }
}
