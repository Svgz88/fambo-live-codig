package com.example.demo.user.domain.model;

import java.util.Set;

public record AccessTokenPayLoad(
        String email, String username, Set<String> roles
) {
}
