package com.example.demo.user.domain.port.in;

public record LoginCommand(
        String email,
        String password
) {
}
