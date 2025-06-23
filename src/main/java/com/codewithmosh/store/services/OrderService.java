package com.codewithmosh.store.services;

import com.codewithmosh.store.dto.OrderDto;
import com.codewithmosh.store.exceptions.OrderNotFoundException;
import com.codewithmosh.store.mappers.OrderMapper;
import com.codewithmosh.store.repositories.OrderRepository;
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