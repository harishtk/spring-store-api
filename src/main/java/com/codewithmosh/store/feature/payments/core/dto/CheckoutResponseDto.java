package com.codewithmosh.store.feature.payments.core.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CheckoutResponseDto {
    private Long orderId;
    private String checkoutUrl;
}
