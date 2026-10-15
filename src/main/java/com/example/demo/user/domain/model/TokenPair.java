package com.example.demo.user.domain.model;

public record TokenPair(
        String accessToken,
        String refreshToken
) {
}
