package com.example.contoso.dto.response.request;

import com.example.contoso.dto.response.request.R;
import com.example.contoso.entity.enums.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author Neevels
 * @version 1.0
 * @date 4/28/2023 12:45 AM
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
public class RequestResponse {
    Integer requestId;
    String clientEmail;
    @JsonFormat(pattern="dd.MM.yyyy")
    LocalDateTime dateTime;
    @JsonFormat(pattern="dd.MM.yyyy")
    Date dateOfDelivery;
    String status;
    List<R> rList;
    String note;
    PaymentMethod paymentMethod;
    String fullName;

}
