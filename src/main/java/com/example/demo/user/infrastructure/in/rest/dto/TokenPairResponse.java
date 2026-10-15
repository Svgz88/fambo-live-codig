package com.example.demo.user.infrastructure.in.rest.dto;

public record TokenPairResponse(
        String accessToken,
        String refreshToken
) {
}
