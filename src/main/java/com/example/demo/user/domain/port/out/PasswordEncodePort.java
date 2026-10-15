package com.example.demo.user.domain.port.out;

public interface PasswordEncodePort {

    String encode(String password);

    boolean matches(String rawPassword, String encodePassword);
}
