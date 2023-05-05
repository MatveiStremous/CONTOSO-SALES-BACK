package com.example.contoso.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * @author Neevels
 * @version 1.0
 * @date 4/27/2023 10:21 AM
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @Column(unique = true)
    private Integer code;
    private int reservedAmount;
    private int amount;
    private Double price;
//    private boolean isActive;

}
