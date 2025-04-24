package com.backend.common.events;


import com.backend.common.dto.CartItem;

import java.time.Instant;
import java.util.List;

public class CartCheckedOutEvent {
    private Long userId;
    private List<CartItem> items;
    private String shippingAddress;
    private String paymentMethod;
    private Instant checkoutTime;


    public CartCheckedOutEvent() {

    }

    public CartCheckedOutEvent(Long userId, List<CartItem> items, String paymentMethod, String shippingAddress, Instant checkoutTime) {
        this.userId = userId;
        this.items = items;
        this.paymentMethod = paymentMethod;
        this.shippingAddress = shippingAddress;
        this.checkoutTime = checkoutTime;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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