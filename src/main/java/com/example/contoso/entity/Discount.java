package com.example.contoso.entity;

import com.example.contoso.entity.enums.DISCOUNT_TYPE;
import jakarta.persistence.*;
import lombok.*;

/**
 * @author Neevels
 * @version 1.0
 * @date 4/27/2023 10:42 AM
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer discountType;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id")
    private Client client;
}
