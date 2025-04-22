package com.backend.notificationservice.domain.service;

import com.backend.notificationservice.domain.model.NotificationEvent;
import com.backend.notificationservice.domain.model.NotificationType;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public interface NotificationSender {
    boolean supports(NotificationType type);
    Mono<Void> send(NotificationEvent event);
}
