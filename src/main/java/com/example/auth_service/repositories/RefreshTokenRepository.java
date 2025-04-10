package com.example.auth_service.repositories;

import com.example.auth_service.entities.RefreshTokenEntity;

import java.util.Optional;

public interface RefreshTokenRepository extends GeneralRepository<RefreshTokenEntity, Long> {
    void delete(RefreshTokenEntity refreshToken);
    int deleteByUserId(Long userId);
    Optional<RefreshTokenEntity> findByToken(String token);
    Optional<RefreshTokenEntity> findByUserLogin(String login);
}
