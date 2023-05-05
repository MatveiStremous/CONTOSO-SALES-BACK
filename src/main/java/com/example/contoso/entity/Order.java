package com.example.contoso.entity;

import com.example.contoso.entity.enums.OrderStatus;
import com.example.contoso.entity.enums.PaymentMethod;
import com.example.contoso.entity.enums.StatusOfRequest;
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
    private Double finalPrice;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<RequestPart> listRequest;


}
