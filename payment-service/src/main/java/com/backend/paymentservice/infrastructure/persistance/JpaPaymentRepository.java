package com.backend.paymentservice.infrastructure.persistance;


import com.backend.paymentservice.domain.model.PaymentTransaction;
import com.backend.paymentservice.domain.repository.PaymentTransactionRepository;
import org.springframework.stereotype.Repository;

@Repository
public class JpaPaymentRepository implements PaymentTransactionRepository {

    private final SpringDataJpaPaymentRepository jpaRepo;

    public JpaPaymentRepository(SpringDataJpaPaymentRepository jpaRepo) {
        this.jpaRepo = jpaRepo;
    }

    @Override
    public PaymentTransaction save(PaymentTransaction paymentTransaction) {
        return jpaRepo.save(paymentTransaction);
    }
}
