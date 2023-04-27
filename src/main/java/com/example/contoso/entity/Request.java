package com.example.contoso.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
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
    private LocalDateTime time;
    private StatusOfRequest status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToMany(mappedBy = "requestList")
    private List<Product> productList;

    enum StatusOfRequest {
        OPEN, ACCEPTED
    }
    @PrePersist
    private void init() {
        time = LocalDateTime.now();
    }
}
