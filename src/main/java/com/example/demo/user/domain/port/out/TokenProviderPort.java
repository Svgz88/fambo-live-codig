package com.example.demo.user.domain.port.out;

import com.example.demo.user.domain.model.AccessTokenPayLoad;
import com.example.demo.user.domain.model.User;

import java.util.Optional;
import java.util.UUID;

public interface TokenProviderPort {
    // Generera en JWT sträng baserat på användaren(email)
    //Varför port.out? För att beroende till infrastrukturen ligger utanför min affärslogik,
    String generateAccessToken(User user);

    String generateRefreshToken (UUID tokenId);

    Optional<AccessTokenPayLoad> extractAccessPayLoad(String token);

    Optional<String> extractRefreshPayload (String token);
}