package com.example.contoso.controller;

import com.example.contoso.dto.request.user.ChangePasswordRequest;
import com.example.contoso.dto.request.user.LoginRequest;
import com.example.contoso.dto.response.user.UserResponse;
import com.example.contoso.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Neevels
 * @version 1.0
 * @date 4/27/2023 11:56 AM
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        userService.changePassword(changePasswordRequest);
        return ResponseEntity
                .ok()
                .body("Password changed");
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity
                .ok()
                .body(userService.login(loginRequest));
    }


}
