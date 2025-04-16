package com.example.auth_service.config;

import com.example.auth_service.security.TokenHolder;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfig {
    private final TokenHolder tokenHolder;

    public FeignClientConfig(TokenHolder tokenHolder) {
        this.tokenHolder = tokenHolder;
    }

//    @Bean
//    public RequestInterceptor requestInterceptor() {
//        return requestTemplate -> {
//            String token = tokenHolder.getToken();
//            if (token != null) {
//                requestTemplate.header("Authorization", "Bearer " + token);
//            }
//        };
//    }

}
