package com.codewithmosh.store.feature.payments.core.service;

import com.codewithmosh.store.feature.cart.entity.Cart;
import com.codewithmosh.store.feature.payments.core.dto.CheckoutResponseDto;
import com.codewithmosh.store.feature.orders.entity.Order;
import com.codewithmosh.store.feature.cart.exception.CartEmptyException;
import com.codewithmosh.store.feature.cart.exception.CartNotFoundException;
import com.codewithmosh.store.feature.payments.core.model.PaymentResult;
import com.codewithmosh.store.core.models.PaymentStatus;
import com.codewithmosh.store.feature.payments.core.exception.PaymentException;
import com.codewithmosh.store.feature.payments.core.model.WebHookRequest;
import com.codewithmosh.store.feature.cart.repository.CartRepository;
import com.codewithmosh.store.feature.orders.repository.OrderRepository;
import com.codewithmosh.store.feature.authentication.service.AuthService;
import com.codewithmosh.store.feature.cart.service.CartService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CheckoutService {

    private final CartRepository cartRepository;
    private final CartService cartService;
    private final OrderRepository orderRepository;
    private final AuthService authService;
    private final PaymentGateway paymentGateway;

    @Transactional
    public CheckoutResponseDto checkout(UUID cartId) throws PaymentException {
        var cart = validateCart(cartId);
        var user = authService.getCurrentUser();
        var order = Order.fromCart(cart, user);

        orderRepository.save(order);

        try {
            var session = paymentGateway.createCheckoutSession(order);
            cartService.clearCart(cartId);
            return new CheckoutResponseDto(order.getId(), session.getCheckoutUrl());
        } catch (PaymentException e) {
            orderRepository.delete(order);
            throw e;
        }
    }

    private Cart validateCart(UUID cartId) {
        var cart = cartRepository.getCartWithItems(cartId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found"));

        if (cart.isEmpty()) {
            throw new CartEmptyException("Cart is empty");
        }

        return cart;
    }

    public void handleWebhookEvent(WebHookRequest request) {
        paymentGateway.parseWebhookRequest(request)
                .ifPresent(this::processPaymentResult);
    }

    private void processPaymentResult(PaymentResult paymentResult) {
        var order = orderRepository.findById(paymentResult.getOrderId()).orElseThrow();
        order.setStatus(PaymentStatus.PAID);
        orderRepository.save(order);
    }
}

