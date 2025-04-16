package com.example.auth_service.feign;

import com.example.auth_service.config.FeignClientConfig;
import com.example.auth_service.dtos.UserRegistrationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "team-service", configuration = FeignClientConfig.class)
public interface TeamServiceClient {
    @PostMapping("/users/create")
    ResponseEntity<String> register(@RequestBody UserRegistrationRequest registrationRequest);
}

