package com.example.demo.user.infrastructure.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaRefreshTokenEntityRepository extends JpaRepository <RefreshTokenEntity, UUID> {

    void deleteByUserId(UUID userId);

}
