package com.example.demo.user.infrastructure.out.persistence;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "refresh_tokens")
public class RefreshTokenEntity {

    @Id
    @Column(name = "token_id")
    private UUID tokenid; // This one send to jwt as jti claim

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    private Instant expiryDate;

    protected RefreshTokenEntity(){}

    public RefreshTokenEntity(UUID tokenId, UserEntity userEntity, Instant expiryDate){
        this.tokenid = tokenId;
        this.user = userEntity;
        this.expiryDate = expiryDate;
    }

    public UserEntity getUserEntity() {
        return user;
    }

    public Instant getExpiryDate() {
        return expiryDate;
    }
}
