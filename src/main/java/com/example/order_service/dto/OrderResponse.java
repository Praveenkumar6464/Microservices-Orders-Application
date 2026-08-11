package com.example.order_service.dto;

public class OrderResponse {

    private Long orderId;
    private Long userId;
    private String product;
    private Double amount;
    private UserResponse user;

    public OrderResponse() {
    }

    public OrderResponse(
            Long orderId,
            Long userId,
            String product,
            Double amount,
            UserResponse user) {

        this.orderId = orderId;
        this.userId = userId;
        this.product = product;
        this.amount = amount;
        this.user = user;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }
}