package com.backend.common.events;


import com.backend.common.dto.OrderItem;

import java.time.LocalDateTime;
import java.util.List;

public class OrderCreatedEvent {
    private String orderId;
    private Long buyerId;
    private double totalAmount;
    private String paymentMethod;
    private List<OrderItem> items;
    private LocalDateTime createdAt;

    public OrderCreatedEvent() {}

    public OrderCreatedEvent(String orderId, Long buyerId, double totalAmount, String paymentMethod, List<OrderItem> items,LocalDateTime createdAt) {
        this.orderId = orderId;
        this.buyerId = buyerId;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.items = items;
        this.createdAt = createdAt;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Long getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}