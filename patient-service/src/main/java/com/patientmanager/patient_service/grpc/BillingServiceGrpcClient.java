package com.patientmanager.patient_service.grpc;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;

@Service
public class BillingServiceGrpcClient {

    @GrpcClient("billing-service")
    private BillingServiceGrpc.BillingServiceBlockingStub billingServiceBlockingStub;

    private static final Logger log = LoggerFactory.getLogger(BillingServiceGrpcClient.class);

    public BillingResponse createBillingAccount(String patientId, String name, String email) {
        BillingRequest request = BillingRequest.newBuilder()
                .setPatientId(patientId)
                .setName(name)
                .setEmail(email)
                .build();

        BillingResponse response = billingServiceBlockingStub.createBillingAccount(request);
        log.info("Billing patient created: {}", response);
        return response;
    }

}
