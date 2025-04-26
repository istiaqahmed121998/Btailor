package com.backend.paymentservice.domain.model;


import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "payment_transaction")
public class PaymentTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id",        nullable = false, length = 50)
    private String orderId;

    @Column(name = "user_id",         nullable = false)
    private Long userId;

    @Column(name = "amount_cents",    nullable = false)
    private double amountCents;

    @Column(nullable = false, length = 20)
    private String method;

    @Column(nullable = false)
    private Boolean success;

    @Column(name = "transaction_id", length = 100)
    private String transactionId;

    @Column(name = "failure_reason")
    private String failureReason;

    @Column(name = "created_at",      nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    public PaymentTransaction() {} // JPA

    public PaymentTransaction(String orderId, Long userId, double amountCents,
                              String method, Boolean success,
                              String transactionId, String failureReason) {
        this.orderId       = orderId;
        this.userId        = userId;
        this.amountCents   = amountCents;
        this.method        = method;
        this.success       = success;
        this.transactionId = transactionId;
        this.failureReason = failureReason;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public double getAmountCents() {
        return amountCents;
    }

    public void setAmountCents(double amountCents) {
        this.amountCents = amountCents;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
