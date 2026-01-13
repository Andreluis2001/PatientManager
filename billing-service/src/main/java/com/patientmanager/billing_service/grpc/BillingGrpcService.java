package com.patientmanager.billing_service.grpc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc.BillingServiceImplBase;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class BillingGrpcService extends BillingServiceImplBase {

    private static final Logger logger = LoggerFactory.getLogger(BillingGrpcService.class);

    @Override
    public void createBillingAccount(BillingRequest request, 
            StreamObserver<BillingResponse> responseObserver
    ){

        logger.info("Received billing account creation request for patient : {}", request);

        BillingResponse response = BillingResponse.newBuilder()
            .setAccountId("123456")
            .setStatus("ACTIVE")
            .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
