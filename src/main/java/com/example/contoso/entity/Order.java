package com.example.contoso.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * @author Neevels
 * @version 1.0
 * @date 4/27/2023 10:32 AM
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Table(name = "`order`")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private PaymentMethod paymentMethod;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToMany(mappedBy = "orderList")
    private List<Product> productList;

    enum PaymentMethod {
        CASH, CARD
    }
}
