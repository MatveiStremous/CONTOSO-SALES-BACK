package com.example.contoso.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

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
    private LocalDateTime time;
    @JsonFormat(pattern="MM.dd.yyyy")
    private Date dateOfDelivery;
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

    public enum StatusOfRequest {
        CANCELLED("Отменена"),
        DECORATED("Оформлена"),
        COMPLETED("Выполнена"),
        ACCEPTED("Подтверждена");

        private final String url;

        StatusOfRequest(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }
    }

    @PrePersist
    private void init() {
        time = LocalDateTime.now();
    }
}
