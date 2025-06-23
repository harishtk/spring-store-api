package com.codewithmosh.store.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDto {
    private OrderItemProductDto product;
    private Integer quantity;
    private BigDecimal totalPrice;
}
