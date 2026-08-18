package com.example.order_service.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.order_service.client.PaymentServiceClient;
import com.example.order_service.client.UserServiceClient;
import com.example.order_service.dto.OrderResponse;
import com.example.order_service.dto.UserResponse;
import com.example.order_service.exception.OrderNotFoundException;

@Service
public class OrderService {

    private final UserServiceClient userServiceClient;
    private final PaymentServiceClient paymentServiceClient;

    private final Map<Long, OrderData> orders = new HashMap<>();

    public OrderService(
            UserServiceClient userServiceClient,
            PaymentServiceClient paymentServiceClient) {

        this.userServiceClient = userServiceClient;
        this.paymentServiceClient = paymentServiceClient;

        // Sample orders
        orders.put(
                1010L,
                new OrderData(
                        1010L,
                        1L,
                        "Laptop",
                        55000.0
                )
        );

        orders.put(
                1011L,
                new OrderData(
                        1011L,
                        2L,
                        "Mobile",
                        25000.0
                )
        );

        orders.put(
                1012L,
                new OrderData(
                        1012L,
                        3L,
                        "Headphones",
                        5000.0
                )
        );
    }

    public OrderResponse getOrderById(Long orderId) {

        // 1. Check whether order exists
        OrderData order = orders.get(orderId);

        if (order == null) {
            throw new OrderNotFoundException(orderId);
        }

        // 2. Call User Service
        UserResponse user =
                userServiceClient.getUserById(order.getUserId());

        // 3. Call Payment Service
        String paymentResponse =
                paymentServiceClient.processPayment(order.getOrderId());

        System.out.println("Payment Service Response: " + paymentResponse);

        // 4. Return order response
        return new OrderResponse(
                order.getOrderId(),
                order.getUserId(),
                order.getProduct(),
                order.getAmount(),
                user
        );
    }

    // In-memory order data
    private static class OrderData {

        private final Long orderId;
        private final Long userId;
        private final String product;
        private final Double amount;

        public OrderData(
                Long orderId,
                Long userId,
                String product,
                Double amount) {

            this.orderId = orderId;
            this.userId = userId;
            this.product = product;
            this.amount = amount;
        }

        public Long getOrderId() {
            return orderId;
        }

        public Long getUserId() {
            return userId;
        }

        public String getProduct() {
            return product;
        }

        public Double getAmount() {
            return amount;
        }
    }
}