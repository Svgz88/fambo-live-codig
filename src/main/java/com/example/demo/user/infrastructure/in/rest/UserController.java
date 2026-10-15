package com.example.demo.user.infrastructure.in.rest;

import com.example.demo.user.domain.model.TokenPair;
import com.example.demo.user.domain.port.in.LoginCommand;
import com.example.demo.user.infrastructure.in.rest.dto.*;
import com.example.demo.user.domain.port.in.UserUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserUseCase useCase;

    public UserController(UserUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterRequest request) {
        useCase.registerUser(request.toCommand());
        return ResponseEntity.status(HttpStatus.CREATED).body("New user created");
    }

    @PostMapping("/login")
    public ResponseEntity<TokenPairResponse> loginUser(@RequestBody LoginCommand loginCommand){
        TokenPair token = useCase.loginUser(loginCommand);

        TokenPairResponse response = new TokenPairResponse(token.accessToken(), token.refreshToken());

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/logout")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<LogoutResponse> logoutUser(){
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authUser != null ? authUser.getName() : "Could not get username";
        useCase.logoutUser(currentUsername);
        return ResponseEntity.ok(new LogoutResponse(currentUsername, "Successfully logged out"));
    }

    @PutMapping("/change-username")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> changeUsername(@RequestBody ChangeUsernameRequest changeUsernameRequest) {
        useCase.changeUsername(changeUsernameRequest.newUsername());
        return ResponseEntity.status(HttpStatus.OK).body("New username changed");
    }

}
