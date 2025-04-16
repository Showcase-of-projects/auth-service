package com.example.auth_service.controllers;

import com.example.auth_service.config.FeignClientConfig;
import com.example.auth_service.dtos.TokenDTO;
import com.example.auth_service.dtos.UserDTO;
import com.example.auth_service.feign.TeamServiceClient;
import com.example.auth_service.services.AuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthenticationService authenticationService;
    private final TeamServiceClient teamServiceClient;

    public AuthController(AuthenticationService authenticationService, TeamServiceClient teamServiceClient) {
        this.authenticationService = authenticationService;
        this.teamServiceClient = teamServiceClient;
    }

    @PostMapping("/register")
    public ResponseEntity<TokenDTO> register(@RequestBody UserDTO userDto, HttpServletResponse response) {
        TokenDTO tokenDTO = authenticationService.register(userDto);


        return ResponseEntity.ok(tokenDTO);
    }

//    @PostMapping("/refresh")
//    public ResponseEntity<TokenDTO> refresh(@RequestParam String refreshToken,HttpServletResponse response) {
//
//        return ResponseEntity.ok(authenticationService.refresh(refreshToken));
//    }

    @PostMapping("/authenticate")
    public ResponseEntity<TokenDTO> authenticate(@RequestParam String login, @RequestParam String password, HttpServletResponse response) {

        TokenDTO tokenDTO = authenticationService.authenticate(login, password);

        return ResponseEntity.ok(tokenDTO);
    }

    @GetMapping("/service")
    public ResponseEntity<String> getServiceToken(@RequestParam String service, @RequestParam String secret) {
        return ResponseEntity.ok(authenticationService.serviceToken(service, secret));
    }
}
