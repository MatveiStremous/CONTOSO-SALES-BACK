package com.example.contoso.dto.mapper;

import com.example.contoso.dto.request.product.ProductRequest;
import com.example.contoso.dto.response.order.OrderResponse;
import com.example.contoso.dto.response.request.R;
import com.example.contoso.entity.Order;
import com.example.contoso.entity.Product;
import com.example.contoso.entity.Request;
import com.example.contoso.entity.enums.OrderStatus;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Neevels
 * @version 1.0
 * @date 4/28/2023 11:40 PM
 */
@NoArgsConstructor
@Component
public class OrderMapper {
    public OrderResponse toResponseDto(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .userFullName(order.getUser().getName() + " " + order.getUser().getSurname())
                .clientEmail(order.getClient().getEmail())
                .finalPrice(order.getFinalPrice())
                .rList(order.getListRequest().stream().map(requestPart ->
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
                .paymentMethod(order.getPaymentMethod().getUrl())
                .status(order.getStatus().getUrl())
                .note(order.getNote())
                .closingDate(order.getClosingDate())
                .dateOfCreate(order.getDateOfCreated())
                .dateOfDelivery(order.getDateOfDelivery())
                .build();
    }

    public Order toOrder(Request request, OrderStatus orderStatus) {
        return Order.builder()
                .status(orderStatus)
                .user(request.getUser())
                .listRequest(request.getListRequest())
                .paymentMethod(request.getPaymentMethod())
                .client(request.getClient())
                .dateOfDelivery(request.getDateOfDelivery())
                .note(request.getNote())
                .build();
    }

}
