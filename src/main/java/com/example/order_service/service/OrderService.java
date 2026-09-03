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
        orders.put(1010L, new OrderData(1010L, 1L, "Laptop", 55000.0));
        orders.put(1011L, new OrderData(1011L, 2L, "Mobile", 25000.0));
        orders.put(1012L, new OrderData(1012L, 3L, "Headphones", 5000.0));
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
                userServiceClientService.getUserWithFallback(order.getUserId());

        // 4. Build response
        // No payment call here — viewing an order should never
        // trigger a new payment attempt. We return the stored status.
        OrderResponse response = new OrderResponse();

        response.setOrderId(order.getOrderId());
        response.setUserId(order.getUserId());
        response.setProduct(order.getProduct());
        response.setAmount(order.getAmount());
        response.setUser(user);
        response.setPaymentStatus(order.getPaymentStatus());

        return response;
    }

    // =========================================================
    // CREATE ORDER
    // =========================================================

    public OrderResponse createOrder(CreateOrderRequest request) {

        // 1. Verify user through Circuit Breaker service
        UserResponse user =
                userServiceClientService.getUserWithFallback(request.getUserId());

        // 2. Generate new order ID
        Long newOrderId = orders.keySet()
                .stream()
                .max(Long::compareTo)
                .orElse(1000L) + 1;

        // 3. Attempt payment.
        //    PaymentServiceClient already wraps this call in its own
        //    circuit breaker + fallback (see fallbackPayment), so this
        //    never throws — it always returns a PaymentResponse, success
        //    or "FAILED_PENDING_RETRY".
        PaymentResponse payment =
                paymentServiceClient.processPayment(newOrderId, request.getAmount());
        String paymentStatus = payment != null ? payment.getStatus() : "PENDING";

        // 4. Create and save order, including the payment outcome
        //    (Saga-style compensation: order is still recorded even if
        //    payment failed, and the failure is reflected honestly.)
        OrderData newOrder = new OrderData(
                newOrderId,
                request.getUserId(),
                request.getProduct(),
                request.getAmount(),
                paymentStatus
        );
        orders.put(newOrderId, newOrder);

        // 5. Build response
        OrderResponse response = new OrderResponse();
        response.setOrderId(newOrderId);
        response.setUserId(request.getUserId());
        response.setProduct(request.getProduct());
        response.setAmount(request.getAmount());
        response.setUser(user);
        response.setPaymentStatus(paymentStatus);

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
        private final String paymentStatus;

        public OrderData(Long orderId, Long userId, String product, Double amount) {
            this(orderId, userId, product, amount, "PENDING");
        }

        public OrderData(Long orderId, Long userId, String product, Double amount, String paymentStatus) {
            this.orderId = orderId;
            this.userId = userId;
            this.product = product;
            this.amount = amount;
            this.paymentStatus = paymentStatus;
        }

        public Long getOrderId() { return orderId; }
        public Long getUserId() { return userId; }
        public String getProduct() { return product; }
        public Double getAmount() { return amount; }
        public String getPaymentStatus() { return paymentStatus; }
    }
}

