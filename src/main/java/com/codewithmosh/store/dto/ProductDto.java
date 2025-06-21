package com.codewithmosh.store.dto;

import lombok.Data;

@Data
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private String price;
    private String categoryId;
}
