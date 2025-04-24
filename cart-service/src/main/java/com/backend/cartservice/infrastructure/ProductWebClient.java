package com.backend.cartservice.infrastructure;

import com.backend.cartservice.application.dto.ProductSnapshotDto;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
@Component
public class ProductWebClient {
    private final WebClient webClient;

    public ProductWebClient(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://localhost:8082").build();
    }

    public Mono<ProductSnapshotDto> getSnapshot(String sku) {
        return webClient.get()
                .uri("/api/products/variants/{sku}", sku)
                .retrieve()
                .bodyToMono(ProductSnapshotDto.class);
    }
}