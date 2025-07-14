package com.dailycodework.buynowdotcom.service.user;

import com.dailycodework.buynowdotcom.dto.UserDto;
import com.dailycodework.buynowdotcom.model.User;
import com.dailycodework.buynowdotcom.request.CreateUserRequest;
import com.dailycodework.buynowdotcom.request.UpdateUserRequest;

public interface IUserService {

    User createUser(CreateUserRequest request);

    User updateUser(UpdateUserRequest request, Long userId);

    User getUserById(Long userId);

    void deleteUser(Long userId);

    UserDto convertToDto(User user);

    User getAuthenticatedUSer();
}
