package com.backend.notificationservice.infrastructure.listener;

import com.backend.notificationservice.application.service.NotificationApplicationService;
import com.backend.notificationservice.application.service.NotificationEventFactory;
import com.backend.notificationservice.application.service.NotificationLogger;
import com.backend.notificationservice.domain.model.NotificationEvent;
import com.backend.notificationservice.domain.model.OrderCreatedEvent;
import com.backend.notificationservice.domain.model.UserCreatedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;


@Component
public class NotificationEventListener {
    private final NotificationApplicationService appService;
    private final NotificationLogger notificationLogger;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public NotificationEventListener(NotificationApplicationService appService, NotificationLogger notificationLogger) {
        this.appService = appService;
        this.notificationLogger = notificationLogger;
    }

    @KafkaListener(topics = "user-events", groupId = "notification-group")
    public void listenUserCreated(ConsumerRecord<String, String> record) throws JsonProcessingException {
        UserCreatedEvent event = objectMapper.readValue(record.value(), UserCreatedEvent.class);
        NotificationEvent notification = NotificationEventFactory.welcomeEmail(event);
        appService.handle(notification)
                .then(Mono.defer(() -> notificationLogger.logSuccess(notification, record.partition(), record.offset(), "USER_CREATED", "SYSTEM")))
                .onErrorResume(e ->
                     notificationLogger.logFailure(notification, record.partition(), record.offset(), "USER_CREATED", "SYSTEM")
                )
                .subscribe();
    }

    @KafkaListener(topics = "order-events", groupId = "notification-group")
    public void listenOrderCreated(String msg) throws JsonProcessingException {
        OrderCreatedEvent event = objectMapper.readValue(msg, OrderCreatedEvent.class);
        NotificationEvent notification = NotificationEventFactory.orderPush(event);
        appService.handle(notification).subscribe();
    }
}
