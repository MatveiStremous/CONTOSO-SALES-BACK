package com.example.contoso.dto.response.product;

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
        String code,
        Integer reservedAmount,
        Integer amount,
        Double price
) {
}
