package com.example.contoso.service.impl;

import com.example.contoso.entity.Client;
import com.example.contoso.entity.Discount;
import com.example.contoso.entity.enums.OrderStatus;
import com.example.contoso.exception.type.BusinessException;
import com.example.contoso.repository.ClientRepository;
import com.example.contoso.repository.DiscountRepository;
import com.example.contoso.repository.OrderRepository;
import com.example.contoso.service.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

/**
 * @author Neevels
 * @version 1.0
 * @date 5/7/2023 5:36 PM
 */
@Service
@RequiredArgsConstructor
public class DiscountServiceImpl implements DiscountService {

    private final OrderRepository orderRepository;
    private final DiscountRepository discountRepository;
    private final ClientRepository clientRepository;

    @Override
    public void updateDiscount(Integer clientId) {
        Discount discount = discountRepository.findByClientId(clientId);
        long count = orderRepository.findAllByClient(getClient(clientId))
                .stream()
                .filter(order -> order.getStatus()
                        .equals(OrderStatus.COMPLETED))
                .count();
        if(count > 3 && count < 10) {
            discount.setDiscountType(5);
            discountRepository.save(discount);
        }
        if(count > 10) {
            discount.setDiscountType(10);
        }
        discountRepository.save(discount);
    }

    @Override
    public Integer getDiscount(Integer clientId) {
        return discountRepository.findByClientId(clientId).getDiscountType();
    }


    private Client getClient(Integer clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new BusinessException("Client not found", HttpStatus.NOT_FOUND));
    }
}
