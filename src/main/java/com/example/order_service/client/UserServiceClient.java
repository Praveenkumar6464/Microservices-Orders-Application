package com.example.order_service.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.example.order_service.dto.UserResponse;
import com.example.order_service.exception.UserNotFoundException;
import com.example.order_service.exception.UserServiceException;

@Component
public class UserServiceClient {

    private final RestClient restClient;

    public UserServiceClient(
            RestClient.Builder restClientBuilder,
            @Value("${user-service.base-url}") String userServiceBaseUrl) {

        this.restClient = restClientBuilder
                .baseUrl(userServiceBaseUrl)
                .build();
    }

    public UserResponse getUserById(Long userId) {

        try {

            return restClient
                    .get()
                    .uri("/api/users/{id}", userId)
                    .retrieve()

                    .onStatus(
                        HttpStatusCode::is4xxClientError,
                        (request, response) -> {
                            if (response.getStatusCode().value() == 404) {
                                throw new UserNotFoundException(userId);
                            }
                        }
                    )

                    .onStatus(
                        HttpStatusCode::is5xxServerError,
                        (request, response) -> {
                            throw new UserServiceException(
                                "User Service returned server error"
                            );
                        }
                    )

                    .body(UserResponse.class);

        } catch (UserNotFoundException ex) {

            throw ex;

        } catch (UserServiceException ex) {

            throw ex;

        } catch (Exception ex) {

            throw new UserServiceException(
                "Unable to communicate with User Service"
            );
        }
    }
} 