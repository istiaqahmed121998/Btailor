package com.backend.notificationservice.application.service;

import com.backend.notificationservice.domain.model.NotificationEvent;
import com.backend.notificationservice.domain.service.NotificationSender;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class NotificationApplicationService {

    private final List<NotificationSender> senders;

    public NotificationApplicationService(List<NotificationSender> senders) {
        this.senders = senders;
    }
    public Mono<Void> handle(NotificationEvent event) {
        return senders.stream()
                .filter(s -> s.supports(event.type()))
                .findFirst()
                .map(sender -> sender.send(event))
                .orElse(Mono.error(new IllegalStateException("No sender found for " + event.type())));
    }
}
