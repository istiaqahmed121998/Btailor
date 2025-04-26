package com.backend.orderservice.infrastructure.messaging;

import com.backend.common.events.*;
import com.backend.orderservice.application.order.service.ApplicationOrderService;
import com.backend.orderservice.domain.model.PaymentRequest;
import com.backend.orderservice.domain.model.PaymentResult;
import com.backend.orderservice.domain.port.PaymentGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrderEventConsumer.class);
    private final ApplicationOrderService applicationOrderService;
    private final PaymentGateway paymentGateway;
    private final InventoryEventPublisher inventoryEventPublisher;
    private final PaymentFailedPublisher paymentFailedPublisher;
    private final OrderCompletePublisher orderCompletePublisher;
    public OrderEventConsumer(ApplicationOrderService applicationOrderService, PaymentGateway paymentGateway, InventoryEventPublisher inventoryEventPublisher, PaymentFailedPublisher paymentFailedPublisher, OrderCompletePublisher orderCompletePublisher) {
        this.applicationOrderService = applicationOrderService;
        this.paymentGateway = paymentGateway;
        this.inventoryEventPublisher = inventoryEventPublisher;
        this.paymentFailedPublisher = paymentFailedPublisher;
        this.orderCompletePublisher = orderCompletePublisher;
    }

    @KafkaListener(topics = "CartCheckedOutEvent", groupId = "order-service-group",containerFactory = "cartCheckedOutListenerFactory")
    public void handleCartCheckedOut(CartCheckedOutEvent event) {
        applicationOrderService.placeOrder(event)
                .doOnNext(order -> {
                    inventoryEventPublisher.publish(new ReserveInventoryRequest(
                            order.getId(), event.getUserId(), event.getItems()
                    ));
                })
                .doOnError(error -> log.info("❌ Error saving order: {}", error.getMessage()))
                .subscribe();

    }
    @KafkaListener(topics = "InventoryReservedEvent", groupId = "order-service-group", containerFactory = "reserveInventoryResponseEventConsumerFactory")
    public void onInventoryReserved(ReserveInventoryResponseEvent event) {
        if(event.success()){
            applicationOrderService.orderStatusUpdate(event.orderId(),"INVENTORY_RESERVED").doOnSuccess(
                            order -> {
                                PaymentResult result = paymentGateway.charge(new PaymentRequest(
                                        order.getId(), order.getBuyerId(), order.getTotalAmount(), order.getPaymentMethod()
                                ));

                                if (result.success()) {
                                    applicationOrderService.orderStatusUpdate(event.orderId(), "COMPLETED")
                                            .subscribe();
                                    orderCompletePublisher.publish(new OrderCompletedEvent(result.transactionId(), order.getBuyerId(),order.getBuyerName(), order.getBuyerEmail(),order.getPaymentMethod(),order.getTotalAmount(),order.getItems()));
                                } else {
                                    paymentFailedPublisher.publish(new PaymentFailedEvent(order.getId(), order.getBuyerId(),order.getBuyerName(), order.getBuyerEmail(),order.getPaymentMethod(),order.getTotalAmount(),result.failureReason(),order.getItems()));
                                }

                            }
                    ).doOnError(error -> log.info(error.getMessage()))
                    .subscribe();

        }
        else {
            applicationOrderService.orderStatusUpdate(event.orderId(), "OUT_OF_STOCK")
                    .subscribe(updatedOrder -> log.info("Order updated: {}", updatedOrder));
        }


    }
}