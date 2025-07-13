package com.backend.paymentservice.application;

import com.backend.paymentservice.domain.model.PaymentTransaction;
import com.backend.paymentservice.domain.repository.PaymentTransactionRepository;
import com.backend.paymentservice.domain.service.PaymentProcessor;
import com.backend.paymentservice.domain.service.PaymentResult;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ProcessPaymentUseCase {
    private final PaymentProcessor processor;
    private final PaymentTransactionRepository repository;
    public ProcessPaymentUseCase(PaymentProcessor processor, PaymentTransactionRepository repository) {
        this.processor = processor;
        this.repository = repository;
    }
    @Transactional
    public PaymentResult process(String orderId, double amount, String method, long userId) {
        PaymentResult result = processor.process(orderId, amount, method, userId);
        var tx = new PaymentTransaction();
        tx.setOrderId(orderId);
        tx.setUserId(userId);
        tx.setAmountCents(amount);
        tx.setMethod(method);
        tx.setSuccess(result.success());
        tx.setTransactionId(result.transactionId());
        tx.setFailureReason(result.failureReason());
        repository.save(tx);
        return result;
    }

}