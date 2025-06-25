package com.codewithmosh.store.feature.orders.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDto {
    private OrderItemProductDto product;
    private Integer quantity;
    private BigDecimal totalPrice;
}
