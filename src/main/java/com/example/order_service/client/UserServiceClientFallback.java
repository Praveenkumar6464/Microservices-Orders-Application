package com.example.order_service.client;

import org.springframework.stereotype.Component;
import com.example.order_service.dto.UserResponse;

@Component
public class UserServiceClientFallback implements UserServiceClient {

    @Override
    public UserResponse getUserById(Long id) {
        UserResponse fallbackUser = new UserResponse();
        fallbackUser.setId(id);
        fallbackUser.setName("Default User (Service Unavailable)");
        fallbackUser.setEmail("N/A");
        return fallbackUser;
    }
}