package com.example.auth_service.services;

import com.example.auth_service.dtos.TokenDTO;
import com.example.auth_service.dtos.UserDTO;
import com.example.auth_service.dtos.UserRegistrationRequest;
import com.example.auth_service.entities.AuthorityEntity;
import com.example.auth_service.entities.UserEntity;
import com.example.auth_service.feign.TeamServiceClient;
import com.example.auth_service.repositories.AuthorityRepository;
import com.example.auth_service.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationService {
    @Value("${service.secret}")
    private String serviceSecret;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityRepository authorityRepository;
    private final ModelMapper modelMapper;
    private final TeamServiceClient teamServiceClient;

    public AuthenticationService(UserRepository userRepository,
                                 AuthenticationManager authenticationManager,
                                 JwtService jwtService,
                                 RefreshTokenService refreshTokenService,
                                 PasswordEncoder passwordEncoder,
                                 AuthorityRepository authorityRepository, ModelMapper modelMapper, TeamServiceClient teamServiceClient) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.modelMapper = modelMapper;
        this.teamServiceClient = teamServiceClient;
    }

    public TokenDTO authenticate(String email, String password) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, password);
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String token = jwtService.generateToken(userDetails);

//        RefreshTokenEntity refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());

        return new TokenDTO(token, null);
    }

    public TokenDTO register(UserDTO userDto) {
        if (userRepository.existsByLogin(userDto.getLogin())) {
            throw new IllegalArgumentException("User with login " + userDto.getLogin() + " already exists");
        }
        List<AuthorityEntity> defaultAuthorities = authorityRepository.findByAuthorityContainingIgnoreCase("user");

        UserEntity userEntity = new UserEntity(
                userDto.getLogin(),
                passwordEncoder.encode(userDto.getPassword()),
                defaultAuthorities
        );
        UserEntity savedUser = userRepository.save(userEntity);
        teamServiceClient.register(new UserRegistrationRequest(
                savedUser.getId(),
                userDto.getName(),
                userDto.getSurname(),
                userDto.getPatronymic(),
                userDto.getGroup(),
                userDto.getRole()
        ));

        String token = jwtService.generateToken(userEntity);

//        RefreshTokenEntity refreshToken = refreshTokenService.createRefreshToken(userEntity.getLogin());

        return new TokenDTO(token, null);
    }

    public String serviceToken(String service, String secret) {
        if (!serviceSecret.equals(secret)) {
            throw new IllegalArgumentException("Invalid secret");
        }
        List<String> authorities = List.of("service");
        String token = jwtService.generateServiceToken(service, authorities);
        return token;
    }
}
