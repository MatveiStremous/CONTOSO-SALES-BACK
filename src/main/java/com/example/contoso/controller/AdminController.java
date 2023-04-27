package com.example.contoso.controller;

import com.example.contoso.dto.request.UserRequest;
import com.example.contoso.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Neevels
 * @version 1.0
 * @date 4/27/2023 11:23 AM
 */
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<String> register(@RequestBody UserRequest userRequest) {
        userService.registration(userRequest);
        return ResponseEntity.ok().body("User successfully registered");
    }
}
