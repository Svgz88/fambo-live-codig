package com.example.demo.user.infrastructure.out.jwt;

import com.example.demo.user.domain.model.AccessTokenPayLoad;
import com.example.demo.user.domain.port.out.TokenProviderPort;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProviderPort tokenProviderPort;

    public JwtAuthenticationFilter(TokenProviderPort tokenProviderPort) {
        this.tokenProviderPort = tokenProviderPort;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        // Extrahera token-sträng. Ta bort "Bearer ", som är 7 tecken.
        String token = authHeader.substring(7);

        Optional<AccessTokenPayLoad> payLoadOptional = tokenProviderPort.extractAccessPayLoad(token);

        if (payLoadOptional.isPresent() && SecurityContextHolder.getContext().getAuthentication() == null) {
            AccessTokenPayLoad payLoad = payLoadOptional.get();

            List<SimpleGrantedAuthority> authorities = payLoad.roles().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    payLoad.email(),
                    null,
                    authorities
            );

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }
}
