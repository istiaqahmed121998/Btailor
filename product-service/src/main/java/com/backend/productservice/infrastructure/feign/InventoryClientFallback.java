package com.backend.productservice.infrastructure.feign;

import com.backend.productservice.exception.ServiceUnavailableException;
import org.springframework.stereotype.Component;

@Component
public class InventoryClientFallback implements InventoryClient {

    @Override
    public void initializeInventory(String variantSku, int quantity) {
        // fallback logic: maybe log or ignore
        System.out.println("Inventory service not available: initializeInventory fallback called");
        throw new ServiceUnavailableException("Inventory service is down. Uncommitting all transactions.");
    }

    @Override
    public Boolean isAvailable(String variantSku, int requiredQuantity) {
        // fallback logic: return false or some safe default
        System.out.println("Inventory service not available: isAvailable fallback called");
        return false;
    }
}