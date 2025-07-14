package com.dailycodework.buynowdotcom.service.order;

import com.dailycodework.buynowdotcom.dto.OrderDto;
import com.dailycodework.buynowdotcom.model.Order;

import java.util.List;

public interface IOrderService {

    Order placeOrder(Long userId);
    List<Order> getUserOrders(Long userId);

    List<OrderDto> convertedOrders(List<Order> orders);

    OrderDto convertToDto(Order order);
}
