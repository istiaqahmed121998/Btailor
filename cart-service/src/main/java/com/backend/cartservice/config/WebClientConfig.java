package com.backend.cartservice.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    @LoadBalanced  // Enables load balancing for service discovery (lb://...)
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }
}