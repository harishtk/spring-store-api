package com.codewithmosh.store.feature.cart.mapper;

import com.codewithmosh.store.feature.cart.dto.CartDto;
import com.codewithmosh.store.feature.cart.dto.CartItemDto;
import com.codewithmosh.store.feature.cart.entity.Cart;
import com.codewithmosh.store.feature.cart.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(target = "totalPrice", expression = "java(cart.getTotalPrice())")
    CartDto toDto(Cart cart);

    @Mapping(target = "totalPrice", expression = "java(cartItem.getTotalPrice())")
    CartItemDto toDto(CartItem cartItem);
}
