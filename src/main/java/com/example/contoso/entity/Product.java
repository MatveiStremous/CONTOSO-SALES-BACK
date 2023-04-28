package com.example.contoso.entity;

import com.example.contoso.dto.request.ProductRequest;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigInteger;
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

    //    @ManyToMany
//    @JoinTable(
//            name = "Product_Request",
//            joinColumns = @JoinColumn(name = "product_id"),
//            inverseJoinColumns = @JoinColumn(name = "request_id")
//    )
//    private List<Request> requestList;
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "client_id")
//    private ProductRequest productRequest;

    @ManyToMany
    @JoinTable(
            name = "Product_Order",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "order_id")
    )
    private List<Order> orderList;

}
