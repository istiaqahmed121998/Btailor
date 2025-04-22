package com.backend.notificationservice.application.service;

import com.backend.notificationservice.domain.model.NotificationEvent;
import com.backend.notificationservice.domain.model.NotificationLog;
import com.backend.notificationservice.domain.repository.NotificationLogRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class NotificationLogger {

    private final NotificationLogRepository repository;

    public NotificationLogger(NotificationLogRepository repository) {
        this.repository = repository;
    }

    public Mono<Void> logSuccess(NotificationEvent event,
                                 int partition,
                                 long offset,
                                 String eventType,
                                 String triggeredBy) {
        return log(event, "SUCCESS", partition, offset, eventType, triggeredBy);
    }

    public Mono<Void> logFailure(NotificationEvent event,
                                 int partition,
                                 long offset,
                                 String eventType,
                                 String triggeredBy) {
        return log(event, "FAILED", partition, offset, eventType, triggeredBy);
    }

    private Mono<Void> log(NotificationEvent event,
                           String status,
                           int partition,
                           long offset,
                           String eventType,
                           String triggeredBy) {

        NotificationLog log = new NotificationLog();
        log.setUserId(event.id());
        log.setType(event.type().name());
        log.setRecipient(event.email());
        log.setSubject(event.subject());
        log.setStatus(status);
        log.setSentAt(LocalDateTime.now());
        log.setKafkaPartition(partition);
        log.setKafkaOffset(offset);
        log.setEventType(eventType);
        log.setTriggeredBy(triggeredBy);

        return repository.save(log).then();
    }
}
