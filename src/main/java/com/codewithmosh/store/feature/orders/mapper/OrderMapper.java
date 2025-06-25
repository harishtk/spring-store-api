package com.codewithmosh.store.feature.orders.mapper;

import com.codewithmosh.store.feature.orders.dto.OrderDto;
import com.codewithmosh.store.feature.orders.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "customerId", source = "customer.id")
    OrderDto toDto(Order order);
}
