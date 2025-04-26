package com.backend.paymentservice.infrastructure.persistance;

import com.backend.paymentservice.domain.model.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SpringDataJpaPaymentRepository extends JpaRepository<PaymentTransaction, Long> {

}
