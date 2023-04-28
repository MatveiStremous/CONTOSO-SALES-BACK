package com.example.contoso.dto.response;

import lombok.*;

/**
 * @author Neevels
 * @version 1.0
 * @date 4/28/2023 2:14 AM
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class R {
    private Integer productId;
    private String name;
    private Integer code;
    private Integer amount;
    private Integer clientAmount;
    private Integer reservedAmount;
    private Double pricePerItem;
}
