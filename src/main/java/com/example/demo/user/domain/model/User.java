package com.example.demo.user.domain.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class User {
    private final UUID id;
    private String username;
    private String encryptedPassword;
    private String email;
    private Set<String> roles;

    // Konstruktör utan id för user register skapelse.
    public User(String username, String password, String email, Set<String> roles) {
        this(null, username, password, email, roles);
    }

    public User(UUID id, String username, String encryptedPassword, String email, Set<String> roles) {
        this.id = id;
        this.encryptedPassword = encryptedPassword;

        setUsername(username);
        changeEmail(email);

        this.roles = (roles != null) ? new HashSet<>(roles) : new HashSet<>();
    }

    public User(UUID id, String email, String username, Set<String> roles){
        this.id = id;
        this.email = email;
        this.username = username;
        this.roles = roles;
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return encryptedPassword;
    }

    public String getEmail() {
        return email;
    }

    public Set<String> getRoles() {
        return Collections.unmodifiableSet(roles);
    }

    public void setUsername(String username) {
        if (username == null || username.length() > 16 || username.length() < 2) {
            throw new IllegalArgumentException("Username must be between 2 and 16 characters");
        }
        this.username = username;
    }

    public void changeEmail(String newEmail) {
        if (newEmail == null || !newEmail.contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        this.email = newEmail;
    }

    public void addRole(String role) {
        if (role != null && !role.isBlank()) {
            this.roles.add(role);
        }
    }

    public boolean isAdmin() {
        return roles.contains("ROLE_ADMIN");
    }
}