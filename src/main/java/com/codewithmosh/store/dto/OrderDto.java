package com.codewithmosh.store.dto;

import com.codewithmosh.store.models.OrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderDto {
    private Long id;
    @JsonIgnore
    private Long customerId;
    private OrderStatus status = OrderStatus.PENDING;
    private BigDecimal totalPrice = BigDecimal.ZERO;
    private Instant createdAt = Instant.now();
    private List<OrderItemDto> items = new ArrayList<>();
}
