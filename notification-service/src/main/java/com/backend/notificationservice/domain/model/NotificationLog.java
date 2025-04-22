package com.backend.notificationservice.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("notification_logs")
public class NotificationLog {

    @Id
    private Long id;

    private Long userId;
    private String type;         // EMAIL, PUSH, etc.
    private String recipient;
    private String subject;
    private String status;       // SUCCESS, FAILED
    private LocalDateTime sentAt;

    @Column("kafka_partition")
    private Integer kafkaPartition;

    @Column("kafka_offset")
    private Long kafkaOffset;

    @Column("event_type")
    private String eventType;    // USER_CREATED, ORDER_CREATED, etc.

    @Column("triggered_by")
    private String triggeredBy;  // SYSTEM, ADMIN, etc.

    public NotificationLog() {

    }

    public NotificationLog(Long id, Long userId, String type, String recipient, String subject, String status, LocalDateTime sentAt, Integer kafkaPartition, Long kafkaOffset, String eventType, String triggeredBy) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.recipient = recipient;
        this.subject = subject;
        this.status = status;
        this.sentAt = sentAt;
        this.kafkaPartition = kafkaPartition;
        this.kafkaOffset = kafkaOffset;
        this.eventType = eventType;
        this.triggeredBy = triggeredBy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTriggeredBy() {
        return triggeredBy;
    }

    public void setTriggeredBy(String triggeredBy) {
        this.triggeredBy = triggeredBy;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Long getKafkaOffset() {
        return kafkaOffset;
    }

    public void setKafkaOffset(Long kafkaOffset) {
        this.kafkaOffset = kafkaOffset;
    }

    public Integer getKafkaPartition() {
        return kafkaPartition;
    }

    public void setKafkaPartition(Integer kafkaPartition) {
        this.kafkaPartition = kafkaPartition;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}