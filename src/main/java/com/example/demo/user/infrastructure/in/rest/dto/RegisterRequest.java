package com.example.demo.user.infrastructure.in.rest.dto;

import com.example.demo.user.domain.port.in.RegisterUserCommand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Username is required")
        String username,

        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 16, message = "8 characters long")
        String rawPassword,

        @NotBlank(message = "Confirm password is required")
        @Size(min = 8, max = 16, message = "8 characters long")
        String confirmRawPassword,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email
        ) {

        public RegisterUserCommand toCommand(){
                return new RegisterUserCommand(username, rawPassword, confirmRawPassword, email);
        }
}
