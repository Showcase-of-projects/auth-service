package com.example.auth_service.repositories;

import com.example.auth_service.entities.AuthorityEntity;

import java.util.List;

public interface AuthorityRepository extends GeneralRepository<AuthorityEntity, Long> {

    boolean existsByAuthority(String authority);

    List<AuthorityEntity> findByAuthorityContainingIgnoreCase(String authority);
}
