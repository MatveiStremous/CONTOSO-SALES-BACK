package com.example.contoso.service;

import com.example.contoso.dto.request.ChangePasswordRequest;
import com.example.contoso.dto.request.UserRequest;

/**
 * @author Neevels
 * @version 1.0
 * @date 4/27/2023 11:23 AM
 */
public interface UserService {
    void registration(UserRequest userRequest);
    void changePassword(ChangePasswordRequest changePasswordRequest);
}
