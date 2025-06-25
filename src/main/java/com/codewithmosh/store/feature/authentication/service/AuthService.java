package com.codewithmosh.store.feature.authentication.service;

import com.codewithmosh.store.feature.authentication.dto.request.LoginRequestDto;
import com.codewithmosh.store.feature.authentication.dto.response.LoginResponseDto;
import com.codewithmosh.store.feature.authentication.model.Jwt;
import com.codewithmosh.store.feature.users.entity.User;
import com.codewithmosh.store.feature.users.exception.UserNotFoundException;
import com.codewithmosh.store.feature.users.service.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthService {


    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public User getCurrentUser() {
        // Get user object from auth
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var userId = (Long) auth.getPrincipal();

        return userRepository.findById(userId).orElse(null);
    }

    public LoginResponseDto login(LoginRequestDto request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));
        final var accessToken = jwtService.generateAccessToken(user);
        final var refreshToken = jwtService.generateRefreshToken(user);

        return new LoginResponseDto(accessToken, refreshToken);
    }

    public Jwt refreshAccessToken(String refreshToken) {
        var jwt = jwtService.parse(refreshToken);
        if (jwt == null || jwt.isExpired()) {
            throw new BadCredentialsException("Invalid refresh token");
        }

        var user = userRepository.findById(jwt.getUserId()).orElseThrow();
        return jwtService.generateAccessToken(user);
    }
}
