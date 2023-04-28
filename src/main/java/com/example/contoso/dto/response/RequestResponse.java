package com.example.contoso.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
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
    @JsonFormat(pattern="MM.dd.yyyy")
    LocalDateTime dateTime;
    @JsonFormat(pattern="MM.dd.yyyy")
    Date dateOfDelivery;
    String status;
    List<R> rList;
    String note;

}
