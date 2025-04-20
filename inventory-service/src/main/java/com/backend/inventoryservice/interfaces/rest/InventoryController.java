package com.backend.inventoryservice.interfaces.rest;

import com.backend.inventoryservice.domain.service.InventoryApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryApplicationService inventoryService;

    public InventoryController(InventoryApplicationService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping
    public ResponseEntity<Void> initializeInventory(@RequestParam("variantSku") String variantSku , @RequestParam("quantity") int quantity){
        inventoryService.initializeInventory(variantSku, quantity);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/availability")
    public ResponseEntity<Boolean> isAvailable(@RequestParam String variantSku,
                                               @RequestParam int requiredQuantity) {
        boolean available = inventoryService.isAvailable(variantSku, requiredQuantity);
        return ResponseEntity.ok(available);
    }
}