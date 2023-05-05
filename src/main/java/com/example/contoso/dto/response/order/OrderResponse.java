package com.example.contoso.dto.response.order;

import com.example.contoso.entity.Client;
import com.example.contoso.entity.RequestPart;
import com.example.contoso.entity.User;
import com.example.contoso.entity.enums.OrderStatus;
import com.example.contoso.entity.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.Builder;

import java.util.List;

/**
 * @author Neevels
 * @version 1.0
 * @date 4/28/2023 11:41 PM
 */
@Builder
public record OrderResponse(
        Integer id,
        Double finalPrice,
        String paymentMethod,
        OrderStatus status,
        String clientEmail,
        String userFullName,
        List<RequestPart> listRequest
) {
}
