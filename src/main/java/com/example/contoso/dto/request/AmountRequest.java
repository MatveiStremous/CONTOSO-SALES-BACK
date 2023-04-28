package com.example.contoso.dto.request;

import lombok.*;

/**
 * @author Neevels
 * @version 1.0
 * @date 4/28/2023 12:35 AM
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AmountRequest {
    private Integer productId;
    private Integer amount;
}
