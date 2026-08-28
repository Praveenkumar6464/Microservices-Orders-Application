package com.example.order_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry; //new
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableRetry //new
@EnableFeignClients/*(basePackages = "com.example.order_service.client")*/
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}
