package com.backend.notificationservice.domain.model;

import java.util.Map;

public record NotificationEvent(Long id,String email, String subject, String template, Map<String, Object> model,NotificationType type) {
}
