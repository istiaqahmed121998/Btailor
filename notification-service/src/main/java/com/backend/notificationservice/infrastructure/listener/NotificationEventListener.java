package com.backend.notificationservice.infrastructure.listener;

import com.backend.common.events.OrderCompletedEvent;
import com.backend.common.events.PaymentFailedEvent;
import com.backend.common.events.UserCreatedEvent;
import com.backend.notificationservice.application.service.NotificationApplicationService;
import com.backend.notificationservice.application.service.NotificationEventFactory;
import com.backend.notificationservice.application.service.NotificationLogger;
import com.backend.notificationservice.domain.model.NotificationEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;


@Component
public class NotificationEventListener {
    private static final Logger log = LoggerFactory.getLogger(NotificationEventListener.class);
    private final NotificationApplicationService appService;
    private final NotificationLogger notificationLogger;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public NotificationEventListener(NotificationApplicationService appService, NotificationLogger notificationLogger) {
        this.appService = appService;
        this.notificationLogger = notificationLogger;
    }

    @KafkaListener(topics = "user-events", groupId = "notification-service-group")
    public void listenUserCreated(ConsumerRecord<String, String> userRecord) throws JsonProcessingException {
        UserCreatedEvent event = objectMapper.readValue(userRecord.value(), UserCreatedEvent.class);
        NotificationEvent notification = NotificationEventFactory.welcomeEmail(event);
        appService.handle(notification)
                .then(Mono.defer(() -> notificationLogger.logSuccess(notification, userRecord.partition(), userRecord.offset(), "USER_CREATED", "SYSTEM")))
                .onErrorResume(e ->
                     notificationLogger.logFailure(notification, userRecord.partition(), userRecord.offset(), "USER_CREATED", "SYSTEM")
                )
                .subscribe();
    }

    @KafkaListener(topics = "OrderCompleted", groupId = "notification-service-group")
    public void listenOrderConfirmed(ConsumerRecord<String, String> orderConfirmed) throws JsonProcessingException {
        OrderCompletedEvent event = objectMapper.readValue(orderConfirmed.value(), OrderCompletedEvent.class);
        NotificationEvent notification = NotificationEventFactory.orderConfirmed(event);
        appService.handle(notification)
                .then(Mono.defer(() -> notificationLogger.logSuccess(notification, orderConfirmed.partition(), orderConfirmed.offset(), "ORDER-CONFIRMED", "SYSTEM")))
                .onErrorResume(e -> {
                    log.info(e.getMessage());
                    return notificationLogger.logFailure(notification, orderConfirmed.partition(), orderConfirmed.offset(), "ORDER-CONFIRMED", "SYSTEM")
                            .then(Mono.empty()); // <-- VERY IMPORTANT
                })
                .subscribe();
    }
    @KafkaListener(topics = "PaymentFailed", groupId = "notification-service-group")
    public void listenPaymentFailed(ConsumerRecord<String, String> paymentFailed) throws JsonProcessingException {
        PaymentFailedEvent event = objectMapper.readValue(paymentFailed.value(), PaymentFailedEvent.class);
        NotificationEvent notification = NotificationEventFactory.paymentFailure(event);
        appService.handle(notification)
                .then(Mono.defer(() -> notificationLogger.logSuccess(notification, paymentFailed.partition(), paymentFailed.offset(), "ORDER-CONFIRMED", "SYSTEM")))
                .onErrorResume(e ->{
                            log.info(e.getMessage());
                            return notificationLogger.logFailure(notification, paymentFailed.partition(), paymentFailed.offset(), "ORDER-CONFIRMED", "SYSTEM");

                })
                .subscribe();
    }
}
