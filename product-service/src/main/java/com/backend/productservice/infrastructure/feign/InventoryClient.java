package com.backend.productservice.infrastructure.feign;

import com.backend.productservice.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
@FeignClient(name = "inventory-service",configuration = FeignConfig.class,fallback = InventoryClientFallback.class)
public interface InventoryClient {
    @PostMapping("/api/inventory")
    void initializeInventory(@RequestParam("variantSku") String variantSku , @RequestParam("quantity") int quantity);
    @GetMapping("/api/inventory/availability")
    Boolean isAvailable(@RequestParam("variantSku") String variantSku, @RequestParam("requiredQuantity") int requiredQuantity);
}
