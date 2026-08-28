package com.example.order_service.service;

import org.springframework.stereotype.Service;

import com.example.order_service.client.UserServiceClient;
import com.example.order_service.dto.UserResponse;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@Service
public class UserServiceClientService {

    private final UserServiceClient userServiceClient;

    public UserServiceClientService(
            UserServiceClient userServiceClient) {

        this.userServiceClient = userServiceClient;
    }

    @CircuitBreaker(
            name = "userService",
            fallbackMethod = "userServiceFallback"
    )
    @Retry(name = "userService")
    @Bulkhead(
            name = "userService",
            type = Bulkhead.Type.SEMAPHORE
    )
    public UserResponse getUserWithFallback(Long userId) {

        System.out.println(
                "Calling User Service for User ID: " + userId
        );

        return userServiceClient.getUserById(userId);
    }

    public UserResponse userServiceFallback(
            Long userId,
            Throwable throwable) {

        System.out.println(
                "USER SERVICE FALLBACK EXECUTED"
        );

        System.out.println(
                "User ID: " + userId
        );

        System.out.println(
                "Failure reason: " + throwable
        );

        UserResponse fallbackUser = new UserResponse();

        fallbackUser.setId(userId);
        fallbackUser.setName(
                "Default User (Service Unavailable)"
        );
        fallbackUser.setEmail("N/A");

        return fallbackUser;
    }
}
