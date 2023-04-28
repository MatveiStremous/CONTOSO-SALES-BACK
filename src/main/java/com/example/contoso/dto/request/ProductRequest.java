package com.example.contoso.dto.request;

import lombok.*;

/**
 * @author Neevels
 * @version 1.0
 * @date 4/27/2023 5:19 PM
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ProductRequest {
    private String name;
    //TODO: code should have 6 digits
    private Integer code;
    private int reservedAmount;
    private int amount;
    private Double price;
}
