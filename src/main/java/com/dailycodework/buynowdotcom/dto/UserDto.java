package com.dailycodework.buynowdotcom.dto;

import com.dailycodework.buynowdotcom.model.Order;
import lombok.Data;

import java.util.List;

@Data
public class UserDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private List<OrderDto> orders;
    private CartDto cart;
}
