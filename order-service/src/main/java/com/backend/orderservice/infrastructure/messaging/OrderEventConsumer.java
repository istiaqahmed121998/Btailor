package com.backend.orderservice.infrastructure.messaging;

import com.backend.common.events.ReserveInventoryRequest;
import com.backend.common.events.ReserveInventoryResponseEvent;
import com.backend.orderservice.application.order.service.ApplicationOrderService;
import com.backend.common.events.CartCheckedOutEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrderEventConsumer.class);
    private final ApplicationOrderService applicationOrderService;
    private final KafkaTemplate<String, ReserveInventoryRequest> kafkaTemplate;

    public OrderEventConsumer(ApplicationOrderService applicationOrderService, KafkaTemplate<String, ReserveInventoryRequest> kafkaTemplate) {
        this.applicationOrderService = applicationOrderService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "cart-checked-out", groupId = "order-service-group",containerFactory = "cartCheckedOutListenerFactory")
    public void handleCartCheckedOut(CartCheckedOutEvent event) {
        applicationOrderService.placeOrder(event)
                .doOnNext(order -> {
                    kafkaTemplate.send("inventory-reserve-request",new ReserveInventoryRequest(
                            order.getId(), event.getUserId(), event.getItems()
                    ));
                })
                .doOnError(error -> log.info("❌ Error saving order: {}", error.getMessage()))
                .subscribe();

    }
    @KafkaListener(topics = "inventory-reserved-response", groupId = "order-service-group", containerFactory = "reserveInventoryResponseEventConsumerFactory")
    public void onInventoryReserved(ReserveInventoryResponseEvent event) {
        applicationOrderService.orderStatusUpdate(event.orderId(),"INVENTORY_RESERVED").doOnSuccess(
                order -> {

//                    kafkaTemplate.send("order-payment-requested", new OrderPaymentRequestedEvent(
//                            order.getId(), order.getBuyerId(), order.getTotalAmount(), order.getPaymentMethod()
//                    ));
                }
        ).doOnError(error -> log.info(error.getMessage()))
                .subscribe();

    }
}