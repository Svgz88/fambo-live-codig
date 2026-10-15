package com.example.demo.user.infrastructure.in.rest.dto;

import java.util.Set;
import java.util.UUID;

public record UserResponse(UUID id, String username, String email, Set<String> role) {}