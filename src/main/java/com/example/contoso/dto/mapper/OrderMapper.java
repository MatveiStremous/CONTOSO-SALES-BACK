package com.example.contoso.dto.mapper;

import com.example.contoso.dto.request.product.ProductRequest;
import com.example.contoso.dto.response.order.OrderResponse;
import com.example.contoso.dto.response.product.ProductResponse;
import com.example.contoso.entity.Order;
import com.example.contoso.entity.Product;
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
                .listRequest(order.getListRequest())
                .paymentMethod(order.getPaymentMethod().getUrl())
                .status(order.getStatus())
                .build();
    }

}
