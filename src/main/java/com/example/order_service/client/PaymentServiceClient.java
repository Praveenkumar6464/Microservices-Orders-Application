package com.example.order_service.client;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.retry.annotation.Retryable;

@Component
public class PaymentServiceClient {

    private final RestClient restClient;

    public PaymentServiceClient(
            RestClient.Builder restClientBuilder,
            @Value("${payment-service.base-url}") String paymentServiceBaseUrl) {

        JdkClientHttpRequestFactory requestFactory =
                new JdkClientHttpRequestFactory();

        requestFactory.setReadTimeout(Duration.ofSeconds(3));

        this.restClient = restClientBuilder
                .baseUrl(paymentServiceBaseUrl)
                .requestFactory(requestFactory)
                .build();
    }

    @Retryable(
            retryFor = RuntimeException.class,
            maxAttempts = 3
    )
    @CircuitBreaker(
            name = "paymentService",
            fallbackMethod = "paymentFallback"
    )
    public String processPayment(Long orderId) {

        System.out.println("Calling Payment Service...");

        return restClient
                .get()
                .uri("/api/payments/{orderId}", orderId)
                .retrieve()
                .body(String.class);
    }

    public String paymentFallback(
            Long orderId,
            Throwable throwable) {

        System.out.println(
                "Payment Service is unavailable. Circuit Breaker activated."
        );

        return "Payment Service is currently unavailable for order "
                + orderId;
    }
}