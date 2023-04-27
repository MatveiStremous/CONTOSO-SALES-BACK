package com.example.contoso.dto.response;

import lombok.Builder;

/**
 * @author Neevels
 * @version 1.0
 * @date 4/27/2023 11:24 AM
 */
@Builder
public record UserResponse (
    String firstName,
    String lastName,
    String login,
    String phoneNumber
) {
}
