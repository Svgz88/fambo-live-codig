package com.example.demo.user.infrastructure.out.jwt;

import com.example.demo.user.domain.model.AccessTokenPayLoad;
import com.example.demo.user.domain.model.User;
import com.example.demo.user.domain.port.out.TokenProviderPort;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Component
public class JwtTokenAdapter implements TokenProviderPort {

    private final SecretKey signingKey;
    private final long ACCESS_TOKEN_VALIDITY = 10*60*1000; // 10 min
    private final long REFRESH_TOKEN_VALIDITY = 7*24*60*60*1000; // 7 days
    private final Date CURRENT_TIME_MS = new Date(System.currentTimeMillis());

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(JwtTokenAdapter.class);

    //Retrieves your secret key from application.properties/yml
    public JwtTokenAdapter(@Value("${app.jwt.secret}") String jwtSecret){
        if (jwtSecret == null || jwtSecret.isBlank()) {
            throw new IllegalArgumentException("JWT Secret must be configured and cannot be blank");
        }
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    //Creates an access token (short-lived, full payload)
    @Override
    public String generateAccessToken(User user) {
        Date expiryDate = new Date(CURRENT_TIME_MS.getTime() + ACCESS_TOKEN_VALIDITY);
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("username", user.getUsername())
                .claim("roles", user.getRoles()) //Set<String> getRoles()
                .issuedAt(CURRENT_TIME_MS)
                .expiration(expiryDate) // + 10 min
                .signWith(this.signingKey)
                .compact();
    }

    //Creates a refresh token (long-lived, sub/email only)
    @Override
    public String generateRefreshToken(UUID tokenId) {
        Date expiryDate = new Date(CURRENT_TIME_MS.getTime() + REFRESH_TOKEN_VALIDITY);
        return Jwts.builder()
                .subject(tokenId.toString())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(expiryDate) // 7 days
                .signWith(signingKey) //can I have other signingKey?
                .compact();
    }

    @Override
    public Optional<AccessTokenPayLoad> extractAccessPayLoad(String token) {
        try{
            Claims claims = Jwts.parser()
                    .verifyWith(this.signingKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String email = claims.getSubject();
            String username = claims.get("username", String.class);
            @SuppressWarnings("unchecked")
            java.util.List<String> rolesList = claims.get("roles", java.util.List.class);
            Set<String> roles = rolesList != null ? Set.copyOf(rolesList) : Set.of();

            return Optional.of(new AccessTokenPayLoad(email,username,roles));

        } catch (JwtException | IllegalArgumentException e) {
            System.err.println("JWT extraction failed: " + e.getMessage());
            //DEBUG
            System.out.println("DEBUG: tomt om token är ogiltigt eller utgången");
            log.error("JWT extraction failed: {}", e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<String> extractRefreshPayload(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(signingKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return Optional.ofNullable(claims.getSubject()); // get String userId
        } catch (JwtException | IllegalArgumentException e){
            log.error("Refresh JWT extraction failed {}", e.getMessage());
            return Optional.empty();
        }
    }

    //TEST
    public static void main(String[] args){

        String secret = "T7LmBhj3txqOs9Iy0zL71AAL/bB0b5G2fSMO/8fXHLQ=";
        JwtTokenAdapter jwtTokenAdapter = new JwtTokenAdapter(secret);

        User user1 = new User(UUID.randomUUID(),"user1","password1","user1@hotmail.com", Set.of("ROLE_USER", "ROLE_ADMIN"));
        User user2 = new User("user2","password2","user2@hotmail.com", Set.of("ROLE_USER"));

        String tokenUser1 = jwtTokenAdapter.generateAccessToken(user1);
        String refresh1 = jwtTokenAdapter.generateRefreshToken(user1.getId());
        Optional<AccessTokenPayLoad> tokenPayLoad = jwtTokenAdapter.extractAccessPayLoad(tokenUser1);

        //do not use .get, use ifPresentOrElse(), .get throw NoSuchElementException, and it crashes whole application.
        tokenPayLoad.ifPresentOrElse(
                payload -> {
                    System.out.println("token: " + payload);
                    System.out.println("rawToken: " + tokenUser1);
                    //System.out.println("username: " + payload.username());
                    //System.out.println("email: " + paeyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI3YmZmNGViMS0xN2VjLTRkMTItYTgwNi1iZjdmNDNkYmUyNTAiLCJpYXQiOjE3OTE4ODMxNjAsImV4cCI6MTc5MjQ4Nzk1OX0.-0Hnt3AlP4ANLCds97k7J8DR5JXg8VKTpNJkI1UdGzMyload.email());
                    //System.out.println("roles: " + payload.roles());
                    System.out.println("refresh token: "+refresh1);
                },
                () -> System.out.println("Could not read the token")
        );


    }
}
