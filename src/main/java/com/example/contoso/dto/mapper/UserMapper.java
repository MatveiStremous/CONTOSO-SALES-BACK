package com.example.contoso.dto.mapper;

import com.example.contoso.dto.request.UserRequest;
import com.example.contoso.dto.response.UserResponse;
import com.example.contoso.entity.User;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Neevels
 * @version 1.0
 * @date 4/27/2023 11:29 AM
 */
@NoArgsConstructor
@Component
public class UserMapper {
    public UserResponse toResponseDto(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getName())
                .lastName(user.getSurname())
                .login(user.getLogin())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole().getUrl())
                .image(user.getImage())
                .build();
    }

    public User toUser(UserRequest userRequest) {
        return User.builder()
                .name(userRequest.getFirstName())
                .surname(userRequest.getLastName())
                .image(userRequest.getImage())
                .login(userRequest.getLogin())
                .password(userRequest.getPassword())
                .phoneNumber(userRequest.getPhoneNumber())
                .role(User.Role.MANAGER)
                .build();
    }
}
