package com.example.contoso.dto.request.order;

import lombok.*;

/**
 * @author Neevels
 * @version 1.0
 * @date 5/6/2023 12:58 PM
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CancelOrderRequest {
    private Integer orderId;
    private String message;
}
