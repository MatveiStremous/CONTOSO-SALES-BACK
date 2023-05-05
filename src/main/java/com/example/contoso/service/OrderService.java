package com.example.contoso.service;

import com.example.contoso.dto.response.order.OrderResponse;
import com.example.contoso.entity.enums.OrderStatus;
import com.example.contoso.entity.enums.StatusOfRequest;

import java.util.List;

/**
 * @author Neevels
 * @version 1.0
 * @date 4/28/2023 11:38 PM
 */
public interface OrderService {
    void changeOrderStatus(Integer id, OrderStatus orderStatus);

    List<OrderResponse> getAll();
}
