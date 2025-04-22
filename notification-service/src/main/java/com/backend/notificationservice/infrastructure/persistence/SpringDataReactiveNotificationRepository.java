package com.backend.notificationservice.infrastructure.persistence;

import com.backend.notificationservice.domain.model.NotificationLog;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;


public interface SpringDataReactiveNotificationRepository extends ReactiveCrudRepository<NotificationLog,Long> {

}
