package com.codewithmosh.store.feature.orders.service;

import com.codewithmosh.store.feature.orders.dto.OrderDto;
import com.codewithmosh.store.feature.orders.exception.OrderNotFoundException;
import com.codewithmosh.store.feature.authentication.service.AuthService;
import com.codewithmosh.store.feature.orders.mapper.OrderMapper;
import com.codewithmosh.store.feature.orders.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class OrderService {

    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final AuthService authService;

    public OrderDto getOrderById(Long orderId) {
        var order = orderRepository.getOrderWithItems(orderId).orElseThrow(() -> new OrderNotFoundException("Order not found"));
        var user = authService.getCurrentUser();

        if (!order.isPlacedBy(user)) {
            throw new AccessDeniedException("You don't have access to this order.");
        }

        return orderMapper.toDto(order);
    }

    public List<OrderDto> getAllOrders() {
        var user = authService.getCurrentUser();
        return orderRepository.getOrdersByCustomer(user).stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }
}