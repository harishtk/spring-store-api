package com.codewithmosh.store.feature.inventory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryDto {
    private Byte id;

    @NotBlank(message = "Name cannot be blank")
    @NotNull(message = "Name is required")
    @Size(min = 4, max = 255, message = "Name cannot exceed 255 characters")
    private String name;
}
