package com.backend.paymentservice.domain.repository;

import com.backend.paymentservice.domain.model.PaymentTransaction;
import org.springframework.stereotype.Repository;

public interface PaymentTransactionRepository {
    PaymentTransaction save(PaymentTransaction paymentTransaction);
}
