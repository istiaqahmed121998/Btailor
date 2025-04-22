package com.backend.notificationservice.infrastructure.email;

import com.backend.notificationservice.domain.model.NotificationEvent;
import com.backend.notificationservice.domain.model.NotificationType;
import com.backend.notificationservice.domain.service.NotificationSender;
import freemarker.template.Configuration;
import freemarker.template.Template;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import reactor.core.publisher.Mono;
@Service
public class ReactiveEmailNotificationService implements NotificationSender {
    private final JavaMailSender mailSender;
    private final Configuration freemarkerConfig;

    public ReactiveEmailNotificationService(JavaMailSender mailSender, Configuration freemarkerConfig) {
        this.mailSender = mailSender;
        this.freemarkerConfig = freemarkerConfig;
    }

    @Override
    public boolean supports(NotificationType type) {
        return type == NotificationType.EMAIL;
    }

    @Override
    public Mono<Void> send(NotificationEvent event) {
        return Mono.fromCallable(() -> {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            Template template = freemarkerConfig.getTemplate("email/" + event.template() + ".ftl");

            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, event.model());

            helper.setTo(event.email());
            helper.setSubject(event.subject());
            helper.setText(html, true);

            mailSender.send(mimeMessage);
            return true;
        }).then();
    }
}
