package com.example.contoso.dto.mapper;

import com.example.contoso.dto.request.request.RequestRequest;
import com.example.contoso.dto.response.order.OrderResponse;
import com.example.contoso.dto.response.request.R;
import com.example.contoso.dto.response.request.RequestResponse;
import com.example.contoso.entity.*;
import com.example.contoso.entity.enums.StatusOfRequest;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Neevels
 * @version 1.0
 * @date 5/7/2023 6:06 PM
 */
@NoArgsConstructor
@Component
public class RequestMapper {

    public Request toRequest(RequestRequest requestRequest, User user, Client client, StatusOfRequest status, List<RequestPart> requestLists) {
        return Request.builder()
                .status(StatusOfRequest.DECORATED)
                .user(user)
                .dateOfDelivery(requestRequest.getDateOfDelivery())
                .note(requestRequest.getNote())
                .listRequest(requestLists)
                .paymentMethod(requestRequest.getPaymentMethod())
                .client(client)
                .build();
    }

    public RequestResponse toResponseDto(Request request) {
        return RequestResponse.builder()
                .requestId(request.getId())
                .clientEmail(request.getClient().getEmail())
                .status(request.getStatus().getUrl())
                .dateTime(request.getTime())
                .dateOfDelivery(request.getDateOfDelivery())
                .note(request.getNote())
                .paymentMethod(request.getPaymentMethod().getUrl())
                .fullName(request.getUser().getName() + " " + request.getUser().getSurname())
                .rList(request.getListRequest()
                        .stream()
                        .map(requestPart ->
                                R.builder()
                                        .reservedAmount(requestPart.getProduct().getReservedAmount())
                                        .amount(requestPart.getProduct().getAmount())
                                        .pricePerItem(requestPart.getProduct().getPrice())
                                        .clientAmount(requestPart.getAmount())
                                        .code(requestPart.getProduct().getCode())
                                        .name(requestPart.getProduct().getName())
                                        .productId(requestPart.getProduct().getId())
                                        .build()
                        )
                        .toList())
                .build();
    }
}
