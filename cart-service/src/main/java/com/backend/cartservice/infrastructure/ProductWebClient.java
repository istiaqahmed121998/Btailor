package com.backend.cartservice.infrastructure;

import com.backend.cartservice.application.dto.ProductSnapshotDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
@Component
public class ProductWebClient {
    private final WebClient webClient;

    public ProductWebClient(@LoadBalanced WebClient.Builder builder,
                            @Value("${service.product.url}") String productServiceUrl) {
        this.webClient = builder.baseUrl(productServiceUrl).build();
    }

    public Mono<ProductSnapshotDto> getSnapshot(String sku) {
        return webClient.get()
                .uri("/api/products/variants/{sku}", sku)
                .retrieve()
                .bodyToMono(ProductSnapshotDto.class);
    }
}