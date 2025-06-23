package com.codewithmosh.store.services;

import com.codewithmosh.store.dto.response.CheckoutResponseDto;
import com.codewithmosh.store.entities.Order;
import com.codewithmosh.store.exceptions.CartEmptyException;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@AllArgsConstructor
@Service
public class CheckoutService {

    private CartService cartService;
    private AuthService authService;

    private CartRepository cartRepository;
    private OrderRepository orderRepository;

    public CheckoutResponseDto checkout(UUID cartId) {
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException("Cart not found");
        }

        if (cart.isEmpty()) {
            throw new CartEmptyException("Cart is empty");
        }

        var user = authService.getCurrentUser();

        Order order = Order.fromCart(cart, user);

        // Save order with items
        orderRepository.save(order);

        cartService.clearCart(cartId);

        return new CheckoutResponseDto(order.getId());
    }
}
