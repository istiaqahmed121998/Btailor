package com.backend.paymentservice.infrastructure.grpc;

import com.backend.paymentservice.application.ProcessPaymentUseCase;
import com.backend.paymentservice.domain.service.PaymentResult;
import io.grpc.stub.StreamObserver;
import org.springframework.grpc.server.service.GrpcService;
import com.backend.common.proto.*;
@GrpcService
public class PaymentGrpcAdapter extends PaymentServiceGrpc.PaymentServiceImplBase {
    private final ProcessPaymentUseCase useCase;

    public PaymentGrpcAdapter(ProcessPaymentUseCase useCase) {
        this.useCase = useCase;
    }

    @Override
    public void processPayment(PaymentProto.PaymentRequest req, StreamObserver<PaymentProto.PaymentResponse> obs) {
        PaymentResult result = useCase.process(
                req.getOrderId(), req.getAmount(), req.getMethod(), req.getUserId()
        );
        PaymentProto.PaymentResponse resp = PaymentProto.PaymentResponse.newBuilder()
                .setSuccess(result.success())
                .setTransactionId(result.transactionId() != null ? result.transactionId() : "")
                .setFailureReason(result.failureReason() != null ? result.failureReason() : "")
                .build();
        obs.onNext(resp);
        obs.onCompleted();
    }
}