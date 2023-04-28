package com.example.contoso.dto.response;

import lombok.Builder;

/**
 * @author Neevels
 * @version 1.0
 * @date 4/29/2023 12:09 AM
 */
@Builder
public record MailResponse (
        String productName,
        Integer productAmount,
        Double price
) {
}
