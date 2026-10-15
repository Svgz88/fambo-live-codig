package com.example.demo.user.application.service;

import com.example.demo.user.domain.model.TokenPair;
import com.example.demo.user.domain.port.in.LoginCommand;
import com.example.demo.user.domain.port.in.RegisterUserCommand;
import com.example.demo.user.domain.port.in.UserUseCase;
import com.example.demo.user.domain.port.out.PasswordEncodePort;
import com.example.demo.user.domain.port.out.RefreshTokenStorePort;
import com.example.demo.user.domain.port.out.TokenProviderPort;
import com.example.demo.user.domain.port.out.UserRepositoryPort;
import com.example.demo.user.domain.model.User;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserUseCase {

    private final UserRepositoryPort userRepositoryPort;
    private final PasswordEncodePort passwordEncodePort;
    private final TokenProviderPort tokenProviderPort;
    private final RefreshTokenStorePort refreshTokenStorePort;
    private final Set<String> USER_ROLE = Set.of("ROLE_USER");

    public UserService(
            UserRepositoryPort userRepositoryPort,
            @Lazy PasswordEncodePort passwordEncodePort,
            TokenProviderPort tokenProviderPort, RefreshTokenStorePort refreshTokenStorePort) {
        this.userRepositoryPort = userRepositoryPort;
        this.passwordEncodePort = passwordEncodePort;
        this.tokenProviderPort = tokenProviderPort;
        this.refreshTokenStorePort = refreshTokenStorePort;
    }

    @Override
    public void registerUser(RegisterUserCommand request) {

        if (userRepositoryPort.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username already taken");
        }
        if (!request.rawPassword().equals(request.confirmRawPassword())) {
            throw new IllegalArgumentException("Password has to match");
        }

        User user =
                new User(
                        request.username(),
                        passwordEncodePort.encode(request.rawPassword()),
                        request.email(),
                        USER_ROLE);

        userRepositoryPort.save(user);
    }

    //TODO: verify emails only.
    @Override
    public TokenPair loginUser(LoginCommand loginCommand) {
        User authUser = userRepositoryPort.findByEmail(loginCommand.email().trim().toLowerCase())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        if (!passwordEncodePort.matches(loginCommand.password(), authUser.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        // Create a unique ID for the database.
        UUID refreshTokenId = UUID.randomUUID();
        // Save session for the database
        Instant expiry = Instant.now().plus(7, ChronoUnit.DAYS);
        refreshTokenStorePort.saveToken(refreshTokenId, authUser, expiry);
        // Generate access token and refresh token
        String accessToken = tokenProviderPort.generateAccessToken(authUser);
        String refreshToken = tokenProviderPort.generateRefreshToken(refreshTokenId);

        return new TokenPair(accessToken,refreshToken);
    }

    @Override
    public TokenPair refreshSession(UUID refreshTokenId) {
        // Find session in the database
        User authUser = refreshTokenStorePort.findUserByTokenId(refreshTokenId)
                .orElseThrow(() -> new BadCredentialsException("Could not find refreshTokenId"));
        // Delete old refresh token
        refreshTokenStorePort.deleteTokenById(refreshTokenId);
        // Create new session with a new UUID token
        UUID newRefreshTokenId = UUID.randomUUID();
        Instant expiryDate = Instant.now().plus(7,ChronoUnit.DAYS);
        refreshTokenStorePort.saveToken(newRefreshTokenId,authUser,expiryDate);
        // Generate new Token Pair
        String newAccessToken = tokenProviderPort.generateAccessToken(authUser);
        String newRefreshToken = tokenProviderPort.generateRefreshToken(newRefreshTokenId);

        return new TokenPair(newAccessToken,newRefreshToken);
    }

    @Override
    public void logoutUser(String username) {
        SecurityContextHolder.clearContext();
    }

    @Override
    public List<User> getAllUsers() {
        return userRepositoryPort.getAllUsers();
    }

    @Override
    public void changeUsername(String newUsername) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //TODO make it more robust.
        String currentUsername = auth.getName();

        User user =
                userRepositoryPort
                        .findByUsername(currentUsername)
                        .orElseThrow(() -> new RuntimeException("User not found"));
        if (userRepositoryPort.existsByUsername(newUsername)) {
            throw new RuntimeException("Username is already taken");
        }

        user.setUsername(newUsername);
        userRepositoryPort.save(user);
    }
}