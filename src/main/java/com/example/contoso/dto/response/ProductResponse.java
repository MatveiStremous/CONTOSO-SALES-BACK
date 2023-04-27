package com.example.contoso.dto.response;

import lombok.Builder;

/**
 * @author Neevels
 * @version 1.0
 * @date 4/27/2023 5:24 PM
 */
@Builder
public record ProductResponse(
        Integer id,
        String name,
        Integer code,
        Long reservedAmount,
        Long freeAmount,
        Double price
) {
}
