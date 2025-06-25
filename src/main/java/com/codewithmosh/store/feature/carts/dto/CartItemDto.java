package com.codewithmosh.store.feature.carts.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItemDto {
    private ProductDto product;
    private Integer quantity;
    private BigDecimal totalPrice;
}
