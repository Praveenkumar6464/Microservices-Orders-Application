package com.example.order_service.client;

//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import com.example.order_service.dto.UserResponse;
//
//
//@FeignClient(name = "USER-SERVICE")
//public interface UserServiceClient {
//
////    @GetMapping("/api/users/{id}")
////    UserResponse getUserById(
////            @PathVariable("id") Long userId
//            
//	@GetMapping("/api/users/{id}")
//            UserResponse getUserById(@PathVariable("id") Long id);
//   // );
//}


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.order_service.dto.UserResponse;

//@FeignClient(
//        name = "USER-SERVICE",
//        fallback = UserServiceClientFallback.class
//)
//public interface UserServiceClient {
//
//    @GetMapping("/api/users/{id}")
//    UserResponse getUserById(@PathVariable("id") Long id);
//}

@FeignClient(name = "USER-SERVICE")
public interface UserServiceClient {

    @GetMapping("/api/users/{id}")
    UserResponse getUserById(@PathVariable("id") Long id);
}