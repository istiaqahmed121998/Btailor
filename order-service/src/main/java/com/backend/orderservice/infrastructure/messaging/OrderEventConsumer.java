package com.backend.orderservice.infrastructure.messaging;

import com.backend.common.events.ReserveInventoryEvent;
import com.backend.common.events.ReserveInventoryResponseEvent;
import com.backend.orderservice.application.order.service.ApplicationOrderService;
import com.backend.common.events.CartCheckedOutEvent;
import com.backend.orderservice.domain.model.Order;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderEventConsumer {

    private final ApplicationOrderService applicationOrderService;
    private final KafkaTemplate<String, ReserveInventoryEvent> kafkaTemplate;

    public OrderEventConsumer(ApplicationOrderService applicationOrderService, KafkaTemplate<String, ReserveInventoryEvent> kafkaTemplate) {
        this.applicationOrderService = applicationOrderService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "cart-checked-out", groupId = "order-service-group")
    public void handleCartCheckedOut(CartCheckedOutEvent event) {
        applicationOrderService.placeOrder(event)
                .doOnNext(order -> {
                    kafkaTemplate.send("reserve-inventory", new ReserveInventoryEvent(
                            order.getId(), event.getUserId(), event.getItems()
                    ));
                    System.out.println("✅ Order saved: " + order);
                })
                .doOnError(error -> System.err.println("❌ Error saving order: " + error.getMessage()))
                .subscribe();

    }
    @KafkaListener(topics = "inventory-reserved", groupId = "order-service-group")
    public void onInventoryReserved(ReserveInventoryResponseEvent event) {
        applicationOrderService.orderStatusUpdate(event.orderId(),"INVENTORY_RESERVED").doOnSuccess(
                order -> {
//                    kafkaTemplate.send("order-payment-requested", new OrderPaymentRequestedEvent(
//                            order.getId(), order.getBuyerId(), order.getTotalAmount(), order.getPaymentMethod()
//                    ));
                }
        ).doOnError(error -> System.err.println("❌ Error saving order: " + error.getMessage()))
                .subscribe();

    }
}