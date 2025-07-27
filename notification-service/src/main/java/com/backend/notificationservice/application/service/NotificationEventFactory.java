package com.backend.notificationservice.application.service;

import com.backend.common.events.OrderCompletedEvent;
import com.backend.common.events.OtpGeneratedEvent;
import com.backend.common.events.PaymentFailedEvent;
import com.backend.common.events.UserCreatedEvent;
import com.backend.notificationservice.domain.model.NotificationEvent;
import com.backend.notificationservice.domain.model.NotificationType;

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

    public static NotificationEvent orderConfirmed(OrderCompletedEvent event) {
        // build the template model
        Map<String, Object> model = new HashMap<>();
        model.put("txnId",         event.txnId());
        model.put("buyerName",     event.buyerName());
        model.put("paymentMethod", event.paymentMethod());
        model.put("totalAmount",   event.totalAmount());
        model.put("items",         event.items());

        // create and return the notification
        return new NotificationEvent(
                event.buyerId(),
                event.buyerEmail(),                // to
                "Your Order is Confirmed!",        // subject
                "order-completion",     // the FreeMarker template name
                model, // data for the template
                NotificationType.EMAIL
        );
    }
    public static NotificationEvent paymentFailure(PaymentFailedEvent event) {
        // 1. Build the template model
        Map<String, Object> model = new HashMap<>();
        model.put("id",            event.id());
        model.put("buyerName",     event.buyerName());
        model.put("buyerEmail",    event.buyerEmail());
        model.put("paymentMethod", event.paymentMethod());
        model.put("totalAmount",   event.totalAmount());
        model.put("failureReason", event.failureReason());
        model.put("items",         event.items());

        // 2. Create and return the notification
        return new NotificationEvent(
                event.buyerId(),
                event.buyerEmail(),              // to
                "Payment Failed for Order “" + event.id() + "”", // subject
                "payment-failure",    // your FreeMarker template
                model,                            // data model for the template
                NotificationType.EMAIL
        );
    }

    public static NotificationEvent forOtp(OtpGeneratedEvent event) {
        // 1. Build the template model
        Map<String, Object> model = new HashMap<>();
        model.put("otpCode", event.otpCode());
        // You could add other details here, like a user's name if it were in the event
        // model.put("name", event.getUserName());

        // 2. Create and return the notification details
        return new NotificationEvent(
                event.userId(),
                event.email(), // to
                "Your Password Reset Code",                     // subject
                "otp-password-reset",                           // your email template name
                model,                                        // data model for the template
                NotificationType.EMAIL
        );
    }
}
