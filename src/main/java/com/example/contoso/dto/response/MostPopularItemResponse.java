package com.example.contoso.dto.response;

import lombok.Builder;

/**
 * @author Neevels
 * @version 1.0
 * @date 5/7/2023 8:42 PM
 */
@Builder
public record MostPopularItemResponse (
        String key,
        Long value
) {
}
