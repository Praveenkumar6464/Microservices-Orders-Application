package com.example.order_service.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.example.order_service.dto.PaymentResponse;

@Component
public class PaymentServiceClient {

    private final RestClient restClient;
    private final CircuitBreakerFactory<?, ?> circuitBreakerFactory;

    public PaymentServiceClient(
            RestClient.Builder restClientBuilder,
            @Value("${payment-service.base-url}") String paymentServiceBaseUrl,
            CircuitBreakerFactory<?, ?> circuitBreakerFactory) {

        this.restClient = restClientBuilder
                .baseUrl(paymentServiceBaseUrl)
                .build();
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    public PaymentResponse processPayment(Long orderId, Double amount) {
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("paymentService");

        return circuitBreaker.run(
            () -> {
                String textResponse = restClient.get()
                        .uri("/api/payments/{orderId}", orderId)
                        .retrieve()
//                        .onStatus(HttpStatusCode::isError, (request, response) -> {
//                            throw new RuntimeException("Payment Service error");
//                        })
                        .onStatus(HttpStatusCode::isError, (request, response) -> {
                            System.out.println("Payment service returned error status: " + response.getStatusCode());
                            throw new RuntimeException("Payment Service error with status: " + response.getStatusCode());
                        })
                        .body(String.class);

                System.out.println("Received from Payment Service: " + textResponse);

                // Build response manually from text
                PaymentResponse paymentResponse = new PaymentResponse();
                paymentResponse.setOrderId(orderId);
                paymentResponse.setAmount(amount);
                paymentResponse.setStatus("SUCCESS"); 
                return paymentResponse;
            },
            throwable -> fallbackPayment(orderId, amount, throwable)
        );
    }

    private PaymentResponse fallbackPayment(Long orderId, Double amount, Throwable throwable) {
        System.out.println("Payment Fallback triggered due to: " + throwable.getMessage());
        return new PaymentResponse(
            null,
            orderId,
            amount,
            "FAILED_PENDING_RETRY"
        );
    }
}