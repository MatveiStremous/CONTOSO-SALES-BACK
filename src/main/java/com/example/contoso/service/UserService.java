package com.example.contoso.service;

import com.example.contoso.dto.request.ChangePasswordRequest;
import com.example.contoso.dto.request.LoginRequest;
import com.example.contoso.dto.request.UserRequest;
import com.example.contoso.dto.response.UserResponse;

import java.util.List;

/**
 * @author Neevels
 * @version 1.0
 * @date 4/27/2023 11:23 AM
 */
public interface UserService {
    void registration(UserRequest userRequest);
    void changePassword(ChangePasswordRequest changePasswordRequest);

    UserResponse login(LoginRequest loginRequest);

    List<UserResponse> getAllManagers();

    void deleteById(Integer id);
}
