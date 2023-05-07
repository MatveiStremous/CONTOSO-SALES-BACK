package com.example.contoso.dto.response.client;

import lombok.Builder;

import java.time.LocalDateTime;

/**
 * @author Neevels
 * @version 1.0
 * @date 4/27/2023 6:12 PM
 */
@Builder
public record ClientResponse(
        Integer id,
        String name,
        String email,
        String address,
        String phoneNumber,
        LocalDateTime dateOfRegistration,
        Integer discount
) {
}
