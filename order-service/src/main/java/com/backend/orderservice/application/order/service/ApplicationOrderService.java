package com.backend.orderservice.application.order.service;

import com.backend.common.dto.OrderItem;
import com.backend.orderservice.domain.model.Order;
import com.backend.orderservice.domain.repository.OrderRepository;
import com.backend.common.events.CartCheckedOutEvent;
import com.backend.common.events.OrderCreatedEvent;
import com.backend.orderservice.infrastructure.messaging.OrderEventPublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class ApplicationOrderService {

    private final OrderRepository orderRepository;
    private final OrderEventPublisher orderEventPublisher;

    public ApplicationOrderService(OrderRepository orderRepository, OrderEventPublisher orderEventPublisher) {
        this.orderRepository = orderRepository;
        this.orderEventPublisher = orderEventPublisher;
    }

    public Mono<Order> placeOrder(CartCheckedOutEvent event) {
        List<OrderItem> orderItems = event.getItems().stream()
                .map(item -> new OrderItem(
                        item.getProductName(),
                        item.getVariantSku(),
                        item.getQuantity(),
                        item.getPrice(),
                        item.getColor(),
                        item.getSize(),
                        item.getThumbnail()
                )).toList();

        Order order = new Order();
        order.setBuyerId(event.getUserId());
        order.setItems(orderItems);
        order.setShippingAddress(event.getShippingAddress());
        order.setPaymentMethod(event.getPaymentMethod());
        order.setStatus("CREATED");
        order.setCreatedAt(LocalDateTime.ofInstant(event.getCheckoutTime(), ZoneId.systemDefault()));

        return orderRepository.save(order)
                .doOnSuccess(saved -> {
                    OrderCreatedEvent createdEvent = new OrderCreatedEvent(
                            order.getId(),
                            order.getBuyerId(),
                            order.getTotalAmount(),
                            order.getPaymentMethod(),
                            order.getItems(),
                            order.getCreatedAt()
                    );
                    orderEventPublisher.publish(createdEvent);
                });
    }

    public  Mono<Order> orderStatusUpdate(String id, String orderStatus) {
        return orderRepository.findById(id).doOnSuccess(order-> order.setStatus(orderStatus));
    }
}