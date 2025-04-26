package com.backend.orderservice.application.order.service;

import com.backend.common.dto.OrderItem;
import com.backend.orderservice.domain.model.Order;
import com.backend.orderservice.domain.repository.OrderRepository;
import com.backend.common.events.CartCheckedOutEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class ApplicationOrderService {

    private final OrderRepository orderRepository;


    public ApplicationOrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
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
        order.setBuyerName(event.getName());
        order.setBuyerEmail(event.getEmail());
        order.setItems(orderItems);
        order.setShippingAddress(event.getShippingAddress());
        order.setPaymentMethod(event.getPaymentMethod());
        order.setStatus("PENDING");
        order.setCreatedAt(LocalDateTime.ofInstant(event.getCheckoutTime(), ZoneId.systemDefault()));

        return orderRepository.save(order);
    }

    public  Mono<Order> orderStatusUpdate(String id, String orderStatus) {
        return orderRepository.findById(id)
                // now chain into save, returning its Mono<Order>
                .flatMap(order -> {
                    order.setStatus(orderStatus);
                    return orderRepository.save(order);
                });
    }
}