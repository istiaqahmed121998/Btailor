package com.backend.orderservice.infrastructure.grpc;

import com.backend.common.proto.PaymentProto;
import com.backend.common.proto.PaymentServiceGrpc;
import com.backend.orderservice.domain.model.PaymentRequest;
import com.backend.orderservice.domain.model.PaymentResult;
import com.backend.orderservice.domain.port.PaymentGateway;
import io.grpc.ManagedChannel;
import org.springframework.grpc.client.GrpcChannelFactory;
import org.springframework.stereotype.Component;

@Component
public class GrpcPaymentGateway implements PaymentGateway {

    private final GrpcChannelFactory channelFactory;

    public GrpcPaymentGateway(GrpcChannelFactory channelFactory) {
        this.channelFactory = channelFactory;
    }

    private PaymentServiceGrpc.PaymentServiceBlockingStub buildHealthyStub() {
        ManagedChannel channel = channelFactory.createChannel("payment");
        if (channel.getState(true) != io.grpc.ConnectivityState.READY) {
            channel.resetConnectBackoff();
            channel.notifyWhenStateChanged(channel.getState(false), () -> {
                // You can log it or handle reconnection
                System.out.println("gRPC Channel State Changed");
            });
        }
        return PaymentServiceGrpc
                .newBlockingStub(channel)
                .withDeadlineAfter(5, java.util.concurrent.TimeUnit.SECONDS);
    }

    @Override
    public PaymentResult charge(PaymentRequest paymentRequest) {
        PaymentProto.PaymentRequest grpcReq = PaymentProto.PaymentRequest.newBuilder()
                .setOrderId(paymentRequest.orderId())
                .setUserId(paymentRequest.userId())
                .setAmount(paymentRequest.amount())
                .setMethod(paymentRequest.method())
                .build();

        PaymentProto.PaymentResponse grpcResp = buildHealthyStub().processPayment(grpcReq);

        return new PaymentResult(
                grpcResp.getSuccess(),
                grpcResp.getTransactionId().isEmpty() ? null : grpcResp.getTransactionId(),
                grpcResp.getFailureReason().isEmpty() ? null : grpcResp.getFailureReason()
        );
    }
}