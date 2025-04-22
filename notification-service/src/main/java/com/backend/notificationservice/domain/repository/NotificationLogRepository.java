package com.backend.notificationservice.domain.repository;

import com.backend.notificationservice.domain.model.NotificationLog;
import reactor.core.publisher.Mono;

public interface NotificationLogRepository {
    Mono<NotificationLog> save(NotificationLog log);
}
