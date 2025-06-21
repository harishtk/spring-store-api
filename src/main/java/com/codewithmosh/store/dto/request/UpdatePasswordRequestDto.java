package com.codewithmosh.store.dto.request;

import lombok.Data;

@Data
public class UpdatePasswordRequestDto {
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}
