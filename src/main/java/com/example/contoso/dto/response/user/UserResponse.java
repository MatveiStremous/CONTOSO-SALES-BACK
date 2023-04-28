package com.example.contoso.dto.response.user;

import lombok.*;

/**
 * @author Neevels
 * @version 1.0
 * @date 4/27/2023 11:24 AM
 */
@Builder
public record UserResponse(
        Integer id,
        String firstName,
        String lastName,
        String login,
        String phoneNumber,
        String role,
        String image
) {
}
