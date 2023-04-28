package com.example.contoso.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * @author Neevels
 * @version 1.0
 * @date 4/27/2023 11:53 PM
 */
@Embeddable
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class RequestPart {
    private Integer amount;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

}
