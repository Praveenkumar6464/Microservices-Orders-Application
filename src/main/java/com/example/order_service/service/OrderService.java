package com.example.order_service.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.order_service.client.PaymentServiceClient;
import com.example.order_service.dto.CreateOrderRequest;
import com.example.order_service.dto.OrderResponse;
import com.example.order_service.dto.PaymentResponse;
import com.example.order_service.dto.UserResponse;
import com.example.order_service.exception.OrderNotFoundException;

@Service
public class OrderService {

    private final UserServiceClientService userServiceClientService;
    private final PaymentServiceClient paymentServiceClient;

    private final Map<Long, OrderData> orders = new HashMap<>();

    public OrderService(
            UserServiceClientService userServiceClientService,
            PaymentServiceClient paymentServiceClient) {

        this.userServiceClientService = userServiceClientService;
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

    // =========================================================
    // GET ORDER BY ID
    // =========================================================

    public OrderResponse getOrderById(Long orderId) {

        // 1. Find order
        OrderData order = orders.get(orderId);

        // 2. Order not found
        if (order == null) {
            throw new OrderNotFoundException(orderId);
        }

        // 3. Get user through Circuit Breaker service
        UserResponse user =
                userServiceClientService.getUserWithFallback(
                        order.getUserId()
                );

        // 4. Process payment
        PaymentResponse payment =
                paymentServiceClient.processPayment(
                        order.getOrderId(),
                        order.getAmount()
                );

        // 5. Build response
        OrderResponse response = new OrderResponse();

        response.setOrderId(order.getOrderId());
        response.setUserId(order.getUserId());
        response.setProduct(order.getProduct());
        response.setAmount(order.getAmount());
        response.setUser(user);

        // 6. Payment status
        response.setPaymentStatus(
                payment != null
                        ? payment.getStatus()
                        : "PENDING"
        );

        return response;
    }

    // =========================================================
    // CREATE ORDER
    // =========================================================

    public OrderResponse createOrder(CreateOrderRequest request) {

        // 1. Verify user through Circuit Breaker service
        UserResponse user =
                userServiceClientService.getUserWithFallback(
                        request.getUserId()
                );

        // 2. Generate new order ID
        Long newOrderId = orders.keySet()
                .stream()
                .max(Long::compareTo)
                .orElse(1000L) + 1;

        // 3. Create order
        OrderData newOrder = new OrderData(
                newOrderId,
                request.getUserId(),
                request.getProduct(),
                request.getAmount()
        );

        // 4. Save order
        orders.put(newOrderId, newOrder);

        // 5. Process payment
        PaymentResponse payment =
                paymentServiceClient.processPayment(
                        newOrderId,
                        request.getAmount()
                );

        // 6. Build response
        OrderResponse response = new OrderResponse();

        response.setOrderId(newOrderId);
        response.setUserId(request.getUserId());
        response.setProduct(request.getProduct());
        response.setAmount(request.getAmount());
        response.setUser(user);

        // 7. Payment status
        response.setPaymentStatus(
                payment != null
                        ? payment.getStatus()
                        : "PENDING"
        );

        return response;
    }

    // =========================================================
    // ORDER DATA
    // =========================================================

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

