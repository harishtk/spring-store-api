package com.codewithmosh.store.feature.payments.core.model;

import com.codewithmosh.store.core.models.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PaymentResult {
    private Long orderId;
    private PaymentStatus paymentStatus;
}
