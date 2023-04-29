package com.example.contoso.service.impl;

import com.example.contoso.entity.enums.StatusOfRequest;
import com.example.contoso.exception.type.BusinessException;
import com.example.contoso.repository.OrderRepository;
import com.example.contoso.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * @author Neevels
 * @version 1.0
 * @date 4/28/2023 11:38 PM
 */
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public void changeOrderStatus(Integer id, StatusOfRequest orderStatus) {
        orderRepository.findById(id)
                .ifPresentOrElse(order -> {

                }, () -> {
                    throw new BusinessException("Заказ не найден", HttpStatus.NOT_FOUND);
                });
    }
}
