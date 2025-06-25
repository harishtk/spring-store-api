package com.codewithmosh.store.feature.carts.mapper;

import com.codewithmosh.store.feature.carts.dto.CartItemDto;
import com.codewithmosh.store.feature.carts.entity.CartItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    CartItemDto toDto(CartItem cartItem);
}
