package com.codewithmosh.store.dto.request;

import lombok.Data;

@Data
public class UpdateUserRequestDto {
    private String name;
    private String email;
}
