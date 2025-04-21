package com.backend.cartservice.infrastructure;

import com.backend.cartservice.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "inventory-client", url = "${inventory.service.url}", configuration = FeignConfig.class)
public interface InventoryClient {
    @GetMapping("/api/inventory/availability")
    Boolean isAvailable(@RequestParam("variantSku") String variantSku, @RequestParam("requiredQuantity") int requiredQuantity);
}