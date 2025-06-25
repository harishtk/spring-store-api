package com.codewithmosh.store.feature.cart.mapper;

import com.codewithmosh.store.feature.cart.dto.CartItemDto;
import com.codewithmosh.store.feature.cart.entity.CartItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    CartItemDto toDto(CartItem cartItem);
}
