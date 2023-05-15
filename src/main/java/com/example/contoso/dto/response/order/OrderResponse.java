package com.example.contoso.dto.response.order;

import com.example.contoso.dto.response.request.R;
import lombok.Builder;

import java.time.LocalDate;
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
        String status,
        String clientEmail,
        Integer clientDiscount,
        String userFullName,
        List<R> rList,
        String note,
        LocalDate dateOfCreate,
        LocalDate dateOfDelivery,
        LocalDate closingDate

) {
}
