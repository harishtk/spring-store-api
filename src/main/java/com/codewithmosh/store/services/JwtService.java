package com.codewithmosh.store.services;

import com.codewithmosh.store.entities.User;
import com.codewithmosh.store.exceptions.UserNotFoundException;
import com.codewithmosh.store.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class JwtService {

    private final SecretKey secretKey;

    public JwtService(@Value("${spring.jwt.secret}") String secret, UserRepository userRepository) {
        secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(User user) {
        var claims = Map.of("email", user.getEmail(), "name", user.getName());

        return Jwts.builder()
                .subject(user.getId().toString())
                .claims(claims)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1)))
                .signWith(secretKey)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            var claims = getClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (JwtException e) {
            return false;
        }
    }

    public Long getUserIdFromToken(String token) {
        return Long.valueOf(getClaims(token).getSubject());
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
