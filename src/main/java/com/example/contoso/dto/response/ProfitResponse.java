package com.example.contoso.dto.response;

import lombok.Builder;

import java.time.LocalDate;

/**
 * @author Neevels
 * @version 1.0
 * @date 5/7/2023 7:30 PM
 */
@Builder
public record ProfitResponse (
        LocalDate key,
        Double value
) {
}
