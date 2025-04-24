package com.backend.cartservice.application.dto;

public record CheckoutRequest (String shippingAddress, String paymentMethod) {}