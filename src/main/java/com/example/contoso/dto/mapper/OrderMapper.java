package com.example.contoso.dto.mapper;

import com.example.contoso.dto.response.order.OrderResponse;
import com.example.contoso.dto.response.request.R;
import com.example.contoso.entity.Order;
import com.example.contoso.entity.Product;
import com.example.contoso.entity.Request;
import com.example.contoso.entity.enums.OrderStatus;
import com.example.contoso.repository.ProductRepository;
import org.springframework.stereotype.Component;

/**
 * @author Neevels
 * @version 1.0
 * @date 4/28/2023 11:40 PM
 */
@Component
public class OrderMapper {
    private final ProductRepository productRepository;

    public OrderMapper(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public OrderResponse toResponseDto(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .userFullName(order.getUser()
                        .getName() + " " + order.getUser()
                        .getSurname())
                .clientEmail(order.getClient()
                        .getEmail())
                .clientDiscount(order.getClient()
                        .getDiscount()
                        .getDiscountType())
                .finalPrice(order.getFinalPrice())
                .rList(order.getListRequest()
                        .stream()
                        .map(requestPart -> {
                                    Product product = productRepository.findById(requestPart.getProductId()).get();
                                    return R.builder()
                                            .reservedAmount(product.getReservedAmount())
                                            .amount(product.getAmount())
                                            .pricePerItem(product.getPrice())
                                            .clientAmount(requestPart.getAmount())
                                            .code(product.getCode())
                                            .name(product.getName())
                                            .productId(product.getId())
                                            .build();
                                }

                        )
                        .toList())
                .paymentMethod(order.getPaymentMethod()
                        .getUrl())
                .status(order.getStatus()
                        .getUrl())
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
