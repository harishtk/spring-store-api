package com.codewithmosh.store.feature.authentication.dto.response;

import com.codewithmosh.store.feature.authentication.model.Jwt;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDto {
    private Jwt accessToken;
    private Jwt refreshToken;
}
