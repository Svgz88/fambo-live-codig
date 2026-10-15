package com.example.demo.user.domain.port.out;

import com.example.demo.user.domain.model.User;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;


public interface RefreshTokenStorePort {

    //check if I need record here.
    void saveToken(UUID tokenId, User user, Instant expiryDate);

    Optional<User> findUserByTokenId(UUID tokenId);

    void deleteTokenById(UUID tokenId);

    void deleteAllTokenForUser(User user);

}
