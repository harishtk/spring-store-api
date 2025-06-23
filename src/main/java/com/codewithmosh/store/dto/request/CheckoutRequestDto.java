package com.codewithmosh.store.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CheckoutRequestDto {
    @NotNull(message = "Cart Id is required")
    private UUID cartId;
}