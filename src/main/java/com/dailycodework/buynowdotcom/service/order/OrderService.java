package com.dailycodework.buynowdotcom.service.order;

import com.dailycodework.buynowdotcom.dto.OrderDto;
import com.dailycodework.buynowdotcom.enums.OrderStatus;
import com.dailycodework.buynowdotcom.model.*;
import com.dailycodework.buynowdotcom.repository.OrderRepository;
import com.dailycodework.buynowdotcom.repository.ProductRepository;
import com.dailycodework.buynowdotcom.service.cart.ICartService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ICartService cartService;
    private final ModelMapper modelMapper;


    @Override
    public Order placeOrder(Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        Order order = createOrder(cart);
        List<OrderItem> orderItemList = createOrderItems(order, cart);
        order.setOrderItems(new HashSet<>(orderItemList));
        order.setTotalAmount(calculateTotalAmount(orderItemList));
        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(cart.getId());
        return savedOrder;
    }

    private Order createOrder(Cart cart){
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return  order;
    }

    private List<OrderItem> createOrderItems(Order order, Cart cart){
        return cart.getItems().stream().map(
                cartItem -> {
                    Product product = cartItem.getProduct();
                    product.setInventory(product.getInventory() - cartItem.getQuantity());
                    productRepository.save(product);
                    return new OrderItem(
                            cartItem.getQuantity(),
                            cartItem.getUnitPrice(),
                            order,
                            product);
                }).toList();
    }


    private BigDecimal calculateTotalAmount(List<OrderItem> orderItemList){
        return orderItemList.stream().map(
                item -> item.getPrice().multiply(new BigDecimal(item.getQuantity()))
        ).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public List<Order> getUserOrders(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public List<OrderDto> convertedOrders(List<Order> orders) {
        return orders.stream().map(this::convertToDto).toList();
        //lambda refrenece will be:  return products.stream().map(product-> convertToDto(product)).toList();
    }
    @Override
    public OrderDto convertToDto(Order order) {
        OrderDto orderDto = modelMapper.map(order, OrderDto.class);
        return orderDto;
    }
}
