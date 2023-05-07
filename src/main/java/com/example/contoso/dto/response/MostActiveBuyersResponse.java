package com.example.contoso.dto.response;

import lombok.Builder;

/**
 * @author Neevels
 * @version 1.0
 * @date 5/7/2023 8:19 PM
 */
@Builder
public record MostActiveBuyersResponse (
        String key,
        Long value
){
}
