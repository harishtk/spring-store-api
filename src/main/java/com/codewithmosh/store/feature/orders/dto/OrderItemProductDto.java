package com.codewithmosh.store.feature.orders.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemProductDto {
    private Long id;
    private String name;
    private BigDecimal price;
}
