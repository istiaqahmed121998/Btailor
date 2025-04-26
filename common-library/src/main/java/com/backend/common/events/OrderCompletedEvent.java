package com.backend.common.events;
import com.backend.common.dto.OrderItem;

import java.util.List;

public record OrderCompletedEvent(String txnId,Long buyerId, String buyerName, String buyerEmail, String paymentMethod, double totalAmount,
                                  List<OrderItem> items) {
}
