package com.backend.orderservice.interfaces.rest;

import com.backend.orderservice.application.order.service.ApplicationOrderService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final ApplicationOrderService applicationOrderService;

    public OrderController(ApplicationOrderService applicationOrderService) {
        this.applicationOrderService = applicationOrderService;
    }

}