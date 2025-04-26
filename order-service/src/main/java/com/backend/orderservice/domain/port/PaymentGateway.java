package com.backend.orderservice.domain.port;

import com.backend.orderservice.domain.model.PaymentRequest;
import com.backend.orderservice.domain.model.PaymentResult;

public interface PaymentGateway {
    PaymentResult charge(PaymentRequest request);
}
