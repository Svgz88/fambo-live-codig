package com.example.demo.user.domain.port.in;

import com.example.demo.user.domain.model.TokenPair;
import com.example.demo.user.domain.model.User;

import java.util.List;
import java.util.UUID;

public interface UserUseCase {
    void registerUser(RegisterUserCommand registerUserCommand);

    TokenPair loginUser(LoginCommand loginCommand);

    TokenPair refreshSession(UUID refreshTokenId);

    void logoutUser(String username);

    List<User> getAllUsers();

    void changeUsername(String newUsername);
}