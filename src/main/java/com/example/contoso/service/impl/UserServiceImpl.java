package com.example.contoso.service.impl;

import com.example.contoso.dto.mapper.UserMapper;
import com.example.contoso.dto.request.ChangePasswordRequest;
import com.example.contoso.dto.request.UserRequest;
import com.example.contoso.dto.response.UserResponse;
import com.example.contoso.entity.User;
import com.example.contoso.exception.type.BusinessException;
import com.example.contoso.repository.UserRepository;
import com.example.contoso.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author Neevels
 * @version 1.0
 * @date 4/27/2023 11:26 AM
 */
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public void registration(UserRequest userRequest) {
        userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        User user = userMapper.toUser(userRequest);
        userRepository.save(user);
    }

    @Override
    public void changePassword(ChangePasswordRequest changePasswordRequest) {
        User user = getUser(changePasswordRequest.getUserId());
        String oldPassword = user.getPassword();
        System.out.println(user.getPassword());

        if (passwordEncoder.matches(changePasswordRequest.getNewPassword(), oldPassword) ||
                changePasswordRequest.getNewPassword()
                        .equals(changePasswordRequest.getOldPassword())
        ) {
            throw new BusinessException("Old and new password are the same. Please, try again!", HttpStatus.CONFLICT);
        }
        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), oldPassword)) {
            throw new BusinessException("Old password doesn't correct. Please, try again!", HttpStatus.BAD_REQUEST);
        }
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        userRepository.save(user);
    }


    private User getUser(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("User not found", HttpStatus.NOT_FOUND));
    }
}
