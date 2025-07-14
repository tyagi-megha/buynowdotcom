package com.dailycodework.buynowdotcom.service.user;

import com.dailycodework.buynowdotcom.dto.UserDto;
import com.dailycodework.buynowdotcom.model.User;
import com.dailycodework.buynowdotcom.repository.UserRepository;
import com.dailycodework.buynowdotcom.request.CreateUserRequest;
import com.dailycodework.buynowdotcom.request.UpdateUserRequest;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User createUser(CreateUserRequest request) {
        return Optional.of(request)
                .filter(user -> !userRepository.existsByEmail(request.getEmail()))
                .map(req -> {
                    User user = new User();
                    user.setEmail(request.getEmail());
                    user.setFirstName(request.getFirstName());
                    user.setLastName(request.getLastName());
                    //earlier it was just get password, hence we need to encode it using bean
                    user.setPassword(passwordEncoder.encode(request.getPassword()));
                    return userRepository.save(user);
                }).orElseThrow(() -> new EntityExistsException("User with email " + request.getEmail() + " already exists"));
    }

    @Override
    public User updateUser(UpdateUserRequest request, Long userId) {
        return userRepository.findById(userId).map(
                existingUser -> {
                    existingUser.setFirstName(request.getFirstName());
                    existingUser.setLastName(request.getLastName());
                    return userRepository.save(existingUser);
                }
        ).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + userId + " not found"));
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId).ifPresentOrElse(userRepository :: delete,
                () -> {
            throw new EntityNotFoundException("User with id " + userId + " not found");
                });
    }

    @Override
    public UserDto convertToDto(User user) {
        UserDto userDto = modelMapper.map(user, UserDto.class);
        return userDto;
    }

    @Override
    public User getAuthenticatedUSer(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return Optional.ofNullable(userRepository.findByEmail(email))
                .orElseThrow(() ->  new EntityNotFoundException("Log in required!"));
    }
}
