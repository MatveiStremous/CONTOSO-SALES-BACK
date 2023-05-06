package com.example.contoso.dto.request.request;

import com.example.contoso.entity.enums.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * @author Neevels
 * @version 1.0
 * @date 4/27/2023 11:18 PM
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RequestRequest {
    private Integer clientId;
    private Integer userId;
    @JsonFormat(pattern="dd.MM.yyyy")
    private LocalDate dateOfDelivery;
    private List<RequestPart> requestLists;
    private String note;
    private PaymentMethod paymentMethod;

}
