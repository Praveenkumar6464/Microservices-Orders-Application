//package com.example.order_service.service;
//
//import org.springframework.stereotype.Service;
//
//import com.example.order_service.client.UserServiceClient;
//import com.example.order_service.dto.OrderResponse;
//import com.example.order_service.dto.UserResponse;
//
//@Service
//public class OrderService {
//
//    private final UserServiceClient userServiceClient;
//
//    public OrderService(UserServiceClient userServiceClient) {
//        this.userServiceClient = userServiceClient;
//    }
//
//    public OrderResponse getOrderById(Long orderId) {
//
//        // Sample order data for Day 1
//        Long userId = 1L;
//        String product = "Laptop";
//        Double amount = 55000.0;
//
//        // Call User Service
//        UserResponse user =
//                userServiceClient.getUserById(userId);
//
//        return new OrderResponse(
//                orderId,
//                userId,
//                product,
//                amount,
//                user
//        );
//    }
//}

//package com.example.order_service.service;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.springframework.stereotype.Service;
//
//import com.example.order_service.client.UserServiceClient;
//import com.example.order_service.dto.OrderResponse;
//import com.example.order_service.dto.UserResponse;
//import com.example.order_service.exception.OrderNotFoundException;
//
//@Service
//public class OrderService {
//
//    private final UserServiceClient userServiceClient;
//
//    private final Map<Long, OrderData> orders = new HashMap<>();
//
//    public OrderService(UserServiceClient userServiceClient) {
//        this.userServiceClient = userServiceClient;
//
//        // Sample order data
//        orders.put(1010L, new OrderData(1010L, 1L, "Laptop", 55000.0));
//        orders.put(1011L, new OrderData(1011L, 2L, "Mobile", 25000.0));
//        orders.put(1012L, new OrderData(1012L, 3L, "Headphones", 5000.0));
//    }
//
//    public OrderResponse getOrderById(Long orderId) {
//
//        // Check whether order exists
//        OrderData order = orders.get(orderId);
//
//        if (order == null) {
//            throw new OrderNotFoundException(orderId);
//        }
//
//        // Get user information from User Service
//        UserResponse user =
//                userServiceClient.getUserById(order.getUserId());
//
//        // Build final response
//        return new OrderResponse(
//                order.getOrderId(),
//                order.getUserId(),
//                order.getProduct(),
//                order.getAmount(),
//                user
//        );
//    }
//
//    // Internal class for in-memory order data
//    private static class OrderData {
//
//        private Long orderId;
//        private Long userId;
//        private String product;
//        private Double amount;
//
//        public OrderData(
//                Long orderId,
//                Long userId,
//                String product,
//                Double amount) {
//
//            this.orderId = orderId;
//            this.userId = userId;
//            this.product = product;
//            this.amount = amount;
//        }
//
//        public Long getOrderId() {
//            return orderId;
//        }
//
//        public Long getUserId() {
//            return userId;
//        }
//
//        public String getProduct() {
//            return product;
//        }
//
//        public Double getAmount() {
//            return amount;
//        }
//    }
//}

//package com.example.order_service.service;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.springframework.stereotype.Service;
//
//import com.example.order_service.client.UserServiceClient;
//import com.example.order_service.dto.OrderResponse;
//import com.example.order_service.dto.UserResponse;
//import com.example.order_service.exception.OrderNotFoundException;
//
//@Service
//public class OrderService {
//
//    private final UserServiceClient userServiceClient;
//
//    private final Map<Long, OrderData> orders = new HashMap<>();
//
//    public OrderService(UserServiceClient userServiceClient) {
//        this.userServiceClient = userServiceClient;
//
//        // In-memory order data
//        orders.put(
//            1010L,
//            new OrderData(
//                1010L,
//                1L,
//                "Laptop",
//                55000.0
//            )
//        );
//
//        orders.put(
//            1011L,
//            new OrderData(
//                1011L,
//                2L,
//                "Mobile",
//                25000.0
//            )
//        );
//
//        orders.put(
//            1012L,
//            new OrderData(
//                1012L,
//                3L,
//                "Headphones",
//                5000.0
//            )
//        );
//    }
//
//    public OrderResponse getOrderById(Long orderId) {
//
//        // Step 1: Check whether the order exists
//        OrderData order = orders.get(orderId);
//
//        if (order == null) {
//            throw new OrderNotFoundException(orderId);
//        }
//
//        // Step 2: Get user information from User Service
//        UserResponse user =
//                userServiceClient.getUserById(order.getUserId());
//
//        // Step 3: Build the final response
//        return new OrderResponse(
//                order.getOrderId(),
//                order.getUserId(),
//                order.getProduct(),
//                order.getAmount(),
//                user
//        );
//    }
//
//    // In-memory order data
//    private static class OrderData {
//
//        private Long orderId;
//        private Long userId;
//        private String product;
//        private Double amount;
//
//        public OrderData(
//                Long orderId,
//                Long userId,
//                String product,
//                Double amount) {
//
//            this.orderId = orderId;
//            this.userId = userId;
//            this.product = product;
//            this.amount = amount;
//        }
//
//        public Long getOrderId() {
//            return orderId;
//        }
//
//        public Long getUserId() {
//            return userId;
//        }
//
//        public String getProduct() {
//            return product;
//        }
//
//        public Double getAmount() {
//            return amount;
//        }
//    }
//}

package com.example.order_service.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.order_service.client.UserServiceClient;
import com.example.order_service.dto.OrderResponse;
import com.example.order_service.dto.UserResponse;
import com.example.order_service.exception.OrderNotFoundException;

@Service
public class OrderService {

    private final UserServiceClient userServiceClient;

    private final Map<Long, OrderData> orders = new HashMap<>();

    public OrderService(UserServiceClient userServiceClient) {

        this.userServiceClient = userServiceClient;

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

        // Check whether the order exists
        OrderData order = orders.get(orderId);

        if (order == null) {
            throw new OrderNotFoundException(orderId);
        }

        // Call User Service through HTTP
        UserResponse user =
                userServiceClient.getUserById(order.getUserId());

        // Create final response
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