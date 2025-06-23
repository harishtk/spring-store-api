package com.codewithmosh.store.controllers;

import com.codewithmosh.store.config.JwtConfig;
import com.codewithmosh.store.dto.request.LoginRequestDto;
import com.codewithmosh.store.dto.response.LoginResponseDto;
import com.codewithmosh.store.dto.response.UserResponseDto;
import com.codewithmosh.store.entities.User;
import com.codewithmosh.store.mappers.UserMapper;
import com.codewithmosh.store.repositories.UserRepository;
import com.codewithmosh.store.services.JwtService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtConfig jwtConfig;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @Valid @RequestBody LoginRequestDto request
    ) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));
        return getLoginResponseDtoResponseEntity(user);
    }

    @GetMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refresh(
            @CookieValue("refreshToken") String refreshToken
    ) {
        var jwt = jwtService.parse(refreshToken);
        if (jwt == null || jwt.isExpired()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var user = userRepository.findById(jwt.getUserId()).orElseThrow();

        return getLoginResponseDtoResponseEntity(user);
    }

    private ResponseEntity<LoginResponseDto> getLoginResponseDtoResponseEntity(User user) {
        final var token = jwtService.generateAccessToken(user);
        final var newRefreshToken = jwtService.generateRefreshToken(user);

        var cookie = ResponseCookie.from("refreshToken", newRefreshToken.toString())
                .httpOnly(true)
                .secure(true)
                .path("/auth")
                .maxAge(jwtConfig.getRefreshTokenExpiration())
                .build();

        return ResponseEntity.ok()
                .header("Set-Cookie", cookie.toString())
                .body(new LoginResponseDto(token.toString()));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> me() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var userId = (Long) auth.getPrincipal();
        var user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Void> handleBadCredentialsException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}