package com.codewithmosh.store.feature.carts.mapper;

import com.codewithmosh.store.feature.carts.dto.CartDto;
import com.codewithmosh.store.feature.carts.dto.CartItemDto;
import com.codewithmosh.store.feature.carts.entity.Cart;
import com.codewithmosh.store.feature.carts.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(target = "totalPrice", expression = "java(cart.getTotalPrice())")
    CartDto toDto(Cart cart);

    @Mapping(target = "totalPrice", expression = "java(cartItem.getTotalPrice())")
    CartItemDto toDto(CartItem cartItem);
}
