package com.backend.notificationservice.application.service;

import com.backend.notificationservice.domain.model.NotificationEvent;
import com.backend.notificationservice.domain.model.NotificationType;
import com.backend.notificationservice.domain.model.OrderCreatedEvent;
import com.backend.notificationservice.domain.model.UserCreatedEvent;

import java.time.Year;
import java.util.HashMap;
import java.util.Map;

public class NotificationEventFactory {

    public static NotificationEvent welcomeEmail(UserCreatedEvent event) {
        Map<String, Object> model = new HashMap<>();
        model.put("name", event.name());
        model.put("loginUrl", "https://btailor.com/login");
        model.put("logoUrl", "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e7/Instagram_logo_2016.svg/2048px-Instagram_logo_2016.svg.png");
        model.put("year", Year.now().getValue());
        model.put("lang", "en");
        model.put("subject", "Welcome to bTailor!");
        model.put("heading", "Welcome"+", "+event.name()+"!");
        model.put("intro", "Your account has been created. Here's what you can do:");
        model.put("feature1", "Track your orders easily");
        model.put("feature2", "Customize your style preferences");
        model.put("feature3", "Receive exclusive deals");
        model.put("ctaText", "Start your journey with bTailor");
        model.put("loginButtonText", "Login Now");
        model.put("disclaimer", "If this wasn't you, please contact support.");

        return new NotificationEvent(
                event.userId(),
                event.email(),
                "👋 Welcome to Our Platform!",
                "user-created",
                model,
                NotificationType.EMAIL
        );
    }

    public static NotificationEvent orderPush(OrderCreatedEvent event) {
        return new NotificationEvent(
                event.userId(),
                event.email(),
                "🛒 Order Placed",
                "order-confirmation",
                Map.of("name", event.name(), "orderId", event.orderId()),
                NotificationType.EMAIL

        );
    }
}
