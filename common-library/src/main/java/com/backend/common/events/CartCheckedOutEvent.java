package com.backend.common.events;


import com.backend.common.dto.CartItem;

import java.time.Instant;
import java.util.List;

public class CartCheckedOutEvent {
    private Long userId;
    private String name;
    private String email;
    private List<CartItem> items;
    private String shippingAddress;
    private String paymentMethod;
    private Instant checkoutTime;


    public CartCheckedOutEvent() {

    }

    public CartCheckedOutEvent(Long userId, String name, String email, List<CartItem> items, String shippingAddress, String paymentMethod, Instant checkoutTime) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.items = items;
        this.shippingAddress = shippingAddress;
        this.paymentMethod = paymentMethod;
        this.checkoutTime = checkoutTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Instant getCheckoutTime() {
        return checkoutTime;
    }

    public void setCheckoutTime(Instant checkoutTime) {
        this.checkoutTime = checkoutTime;
    }
}