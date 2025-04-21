package com.backend.cartservice.domain.event;

import com.backend.cartservice.domain.model.CartItem;

import java.time.Instant;
import java.util.List;

public record CartCheckedOutEvent(Long userId, List<CartItem> items,Long shippingAddressId, String paymentMethod, Instant checkoutTime) {
}
