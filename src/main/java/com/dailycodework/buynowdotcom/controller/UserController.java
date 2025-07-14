package com.dailycodework.buynowdotcom.controller;

import com.dailycodework.buynowdotcom.dto.UserDto;
import com.dailycodework.buynowdotcom.model.User;
import com.dailycodework.buynowdotcom.request.CreateUserRequest;
import com.dailycodework.buynowdotcom.request.UpdateUserRequest;
import com.dailycodework.buynowdotcom.response.ApiResponse;
import com.dailycodework.buynowdotcom.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/users")
public class UserController {
    private final IUserService userService;

    @GetMapping("/user/{userId}/user")
    public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        UserDto userDto = userService.convertToDto(user);
        return ResponseEntity.ok(new ApiResponse("User found", userDto));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> createUser(@RequestBody CreateUserRequest request) {
        User user = userService.createUser(request);
        UserDto userDto = userService.convertToDto(user);
        return ResponseEntity.ok(new ApiResponse("User created", userDto));
    }

    @PutMapping("/user/{userId}/update")
    public ResponseEntity<ApiResponse> updateUser(@RequestBody UpdateUserRequest request,@PathVariable Long userId) {
        User user = userService.updateUser(request, userId);
        UserDto userDto = userService.convertToDto(user);
        return ResponseEntity.ok(new ApiResponse("User updated", userDto));
    }

    @DeleteMapping("/user/{userId}/delete")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(new ApiResponse("User deleted", userId));
    }
}
