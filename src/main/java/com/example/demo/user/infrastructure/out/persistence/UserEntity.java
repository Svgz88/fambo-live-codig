package com.example.demo.user.infrastructure.out.persistence;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/*
Varför gör vi så här? (Lärdom för rapporten)

@ElementCollection: Detta skapar automatiskt en extra tabell i PostgreSQL som heter user_roles.
Den kopplas till din huvudtabell via user_id. Detta är "best practice" för enkla behörigheter.

FetchType.EAGER: När vi hämtar en användare för att kontrollera JWT, vill vi ha rollerna direkt.
Med EAGER slipper vi göra extra frågor till databasen senare (vilket förhindrar LazyInitializationException).

unique = true: Vi ser till att databasen själv stoppar oss om vi försöker skapa två användare med samma namn.
Det är ett extra lager av säkerhet (Data Integrity).
 */

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    private Set<String> roles = new HashSet<>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
