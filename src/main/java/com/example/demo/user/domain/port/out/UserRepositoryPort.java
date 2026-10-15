package com.example.demo.user.domain.port.out;

import com.example.demo.user.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepositoryPort {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    User save(User user);

    boolean existsByUsername(String username);

    List<User> getAllUsers();
}
