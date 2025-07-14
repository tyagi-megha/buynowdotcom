package com.dailycodework.buynowdotcom.controller;

import com.dailycodework.buynowdotcom.dto.OrderDto;
import com.dailycodework.buynowdotcom.model.Order;
import com.dailycodework.buynowdotcom.response.ApiResponse;
import com.dailycodework.buynowdotcom.service.order.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/orders")
public class OrderController {
    private final IOrderService orderService;

    @PostMapping("/user/order")
    public ResponseEntity<ApiResponse> placeOrder(@RequestParam Long userId){
        Order order = orderService.placeOrder(userId);
        OrderDto orderDto = orderService.convertToDto(order);
        return ResponseEntity.ok(new ApiResponse("Order placed successfully!", orderDto));
    }

    @GetMapping("/user/{userId}/order")
    public ResponseEntity<ApiResponse> getUserOrders(@PathVariable Long userId){
        List<Order> orders = orderService.getUserOrders(userId);
        List<OrderDto> orderDtos = orderService.convertedOrders(orders);
        return ResponseEntity.ok(new ApiResponse("Success!", orderDtos));
    }
}
