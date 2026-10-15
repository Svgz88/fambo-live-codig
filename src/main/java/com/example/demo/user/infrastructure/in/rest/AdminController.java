package com.example.demo.user.infrastructure.in.rest;

import com.example.demo.user.domain.port.in.UserUseCase;
import java.util.List;

import com.example.demo.user.infrastructure.in.rest.dto.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserUseCase userUseCase;

    public AdminController(UserUseCase useCase) {
        this.userUseCase = useCase;
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponse>> listResponseUserEntity() {
        List<UserResponse> userResponseList =
                userUseCase.getAllUsers().stream()
                        .map(
                                user ->
                                        new UserResponse(
                                                user.getId(), user.getUsername(), user.getEmail(), user.getRoles()))
                        .toList();
        return ResponseEntity.status(HttpStatus.OK).body(userResponseList);
    }
}
