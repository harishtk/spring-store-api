package com.codewithmosh.store.mappers;

import com.codewithmosh.store.dto.CartItemDto;
import com.codewithmosh.store.entities.CartItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    CartItemDto toDto(CartItem cartItem);
}
