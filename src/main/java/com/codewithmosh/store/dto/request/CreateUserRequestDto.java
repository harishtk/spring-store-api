package com.codewithmosh.store.dto.request;

import lombok.Data;

@Data
public class CreateUserRequestDto {
    private String name;
    private String email;
    private String password;
}
