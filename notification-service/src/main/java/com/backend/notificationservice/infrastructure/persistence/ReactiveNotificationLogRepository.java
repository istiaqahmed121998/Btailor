package com.backend.notificationservice.infrastructure.persistence;

import com.backend.notificationservice.domain.model.NotificationLog;
import com.backend.notificationservice.domain.repository.NotificationLogRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class ReactiveNotificationLogRepository implements NotificationLogRepository {
    private final SpringDataReactiveNotificationRepository notificationRepository;

    public ReactiveNotificationLogRepository(SpringDataReactiveNotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public Mono<NotificationLog> save(NotificationLog log) {
        return notificationRepository.save(log);
    }
}