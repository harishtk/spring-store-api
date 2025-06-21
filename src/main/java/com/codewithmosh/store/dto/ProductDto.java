package com.codewithmosh.store.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class ProductDto {
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must be less than 255 characters")
    private String name;

    @NotBlank(message = "Description is required")
    @Size(max = 255, message = "Description must be less than 255 characters")
    private String description;

    @NotBlank(message = "Price is required")
    @Range(min = 0, max = 999999999, message = "Price must be between 0 and 999999999")
    private String price;

    @NotNull(message = "Category Id is required")
    private Byte categoryId;
}
