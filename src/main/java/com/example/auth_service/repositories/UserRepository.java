package com.example.auth_service.repositories;

import com.example.auth_service.entities.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends GeneralRepository<UserEntity, Long> {

    Optional<UserEntity> findByLogin(String login);

    boolean existsByLogin(String login);
}
