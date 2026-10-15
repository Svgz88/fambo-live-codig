package com.example.demo.user.infrastructure.out.security;

import com.example.demo.user.domain.port.out.PasswordEncodePort;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptPasswordAdapter implements PasswordEncodePort {

    private final PasswordEncoder bCryptPasswordAdapter;

    public BCryptPasswordAdapter(@Lazy PasswordEncoder bCryptPasswordAdapter) {
        this.bCryptPasswordAdapter = bCryptPasswordAdapter;
    }

    @Override
    public String encode(String rawPassword) {
        return bCryptPasswordAdapter.encode(rawPassword);
    }

    @Override
    public boolean matches(String rawPassword, String encodePassword) {
        return bCryptPasswordAdapter.matches(rawPassword,encodePassword);
    }
}
