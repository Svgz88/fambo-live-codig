package com.example.demo.user.domain.port.in;

public record RegisterUserCommand(
        String username,
        String rawPassword,
        String confirmRawPassword,
        String email
) {
}
