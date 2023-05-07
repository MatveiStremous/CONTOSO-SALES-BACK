package com.example.contoso.dto.response;

import com.example.contoso.entity.enums.OrderStatus;
import lombok.Builder;

/**
 * @author Neevels
 * @version 1.0
 * @date 5/7/2023 8:02 PM
 */
@Builder
public record FailedSuccessResponse (
        OrderStatus key,
        Integer value
) {
}
