package com.example.contoso.entity;

import com.example.contoso.entity.enums.PaymentMethod;
import com.example.contoso.entity.enums.StatusOfRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author Neevels
 * @version 1.0
 * @date 4/27/2023 10:41 AM
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @JsonFormat(pattern="MM.dd.yyyy")
    private LocalDate time;
    @JsonFormat(pattern="MM.dd.yyyy")
    private LocalDate dateOfDelivery;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    @Enumerated(EnumType.STRING)
    private StatusOfRequest status;
    private String note;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<RequestPart> listRequest;


    @PrePersist
    private void init() {
        time = LocalDate.now();
    }
}
