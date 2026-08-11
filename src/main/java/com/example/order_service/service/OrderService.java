package com.example.order_service.service;

import org.springframework.stereotype.Service;

import com.example.order_service.client.UserServiceClient;
import com.example.order_service.dto.OrderResponse;
import com.example.order_service.dto.UserResponse;

@Service
public class OrderService {

    private final UserServiceClient userServiceClient;

    public OrderService(UserServiceClient userServiceClient) {
        this.userServiceClient = userServiceClient;
    }

    public OrderResponse getOrderById(Long orderId) {

        // Sample order data for Day 1
        Long userId = 1L;
        String product = "Laptop";
        Double amount = 55000.0;

        // Call User Service
        UserResponse user =
                userServiceClient.getUserById(userId);

        return new OrderResponse(
                orderId,
                userId,
                product,
                amount,
                user
        );
    }
}