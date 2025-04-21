package com.backend.cartservice.config;

import com.backend.cartservice.domain.model.CartItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public ReactiveRedisTemplate<String, CartItem> reactiveRedisTemplate(ReactiveRedisConnectionFactory factory) {
        // Key serializer
        StringRedisSerializer keySerializer = new StringRedisSerializer();

        // ObjectMapper and Value serializer
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        Jackson2JsonRedisSerializer<CartItem> valueSerializer =
                new Jackson2JsonRedisSerializer<>(objectMapper, CartItem.class);

        // Use SerializationPair for the value
        RedisSerializationContext.SerializationPair<CartItem> valuePair =
                RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer);

        // Build context
        RedisSerializationContext<String, CartItem> context = RedisSerializationContext
                .<String, CartItem> newSerializationContext(keySerializer)
                .value(valuePair)
                .build();

        return new ReactiveRedisTemplate<>(factory, context);
    }

}