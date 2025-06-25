package com.codewithmosh.store.feature.orders.dto;

import com.codewithmosh.store.core.models.PaymentStatus;
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
    private PaymentStatus status = PaymentStatus.PENDING;
    private BigDecimal totalPrice = BigDecimal.ZERO;
    private Instant createdAt = Instant.now();
    private List<OrderItemDto> items = new ArrayList<>();
}
