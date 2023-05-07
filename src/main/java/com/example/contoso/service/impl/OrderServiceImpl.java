package com.example.contoso.service.impl;

import com.example.contoso.dto.mapper.OrderMapper;
import com.example.contoso.dto.request.order.CancelOrderRequest;
import com.example.contoso.dto.response.order.OrderResponse;
import com.example.contoso.entity.Product;
import com.example.contoso.entity.enums.OrderStatus;
import com.example.contoso.exception.type.BusinessException;
import com.example.contoso.repository.OrderRepository;
import com.example.contoso.repository.ProductRepository;
import com.example.contoso.service.DiscountService;
import com.example.contoso.service.OrderService;
import com.example.contoso.utils.MailSender;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * @author Neevels
 * @version 1.0
 * @date 4/28/2023 11:38 PM
 */
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final DiscountService discountService;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;
    private final MailSender mailSender;

    @Override
    public void changeOrderStatus(Integer id, OrderStatus orderStatus) {
        orderRepository.findById(id)
                .ifPresentOrElse(order -> {
                    if (Objects.equals(OrderStatus.COMPLETED, orderStatus)) {
                        String userEmail = order.getUser()
                                .getLogin();
                        mailSender.sendMessage(userEmail,
                                "Заказ успешно выполнен",
                                "Поздравляю, заказ с номером "
                                        + order.getId()
                                        + " успешно выполнен!");
                        order.getListRequest()
                                .forEach(requestPart -> {
                                    Product product = requestPart.getProduct();
                                    int amountOfOrder = requestPart.getAmount();
                                    int reservedAmount = product.getReservedAmount();
                                    int amount = product.getAmount();
                                    product.setReservedAmount(reservedAmount - amountOfOrder);
                                    product.setAmount(amount - amountOfOrder);
                                    productRepository.save(product);
                                });
                        order.setClosingDate(LocalDate.now());
                        order.setStatus(OrderStatus.COMPLETED);
                        orderRepository.save(order);
                        discountService.updateDiscount(order.getClient().getId());
                    } else {
                        order.getListRequest()
                                .forEach(requestPart -> {
                                    Product product = requestPart.getProduct();
                                    int amountOfOrder = requestPart.getAmount();
                                    int reservedAmount = product.getReservedAmount();
                                    product.setReservedAmount(reservedAmount - amountOfOrder);
                                    productRepository.save(product);
                                });
                        order.setStatus(OrderStatus.CANCELLED);
                        orderRepository.save(order);
                    }
                }, () -> {
                    throw new BusinessException("Заказ не найден", HttpStatus.NOT_FOUND);
                });
    }

    @Override
    public List<OrderResponse> getAll() {
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::toResponseDto)
                .toList();
    }

    @Override
    public void cancelOrder(CancelOrderRequest cancelOrder) {
        orderRepository.findById(cancelOrder.getOrderId())
                .ifPresentOrElse(order -> {
                    order.getListRequest()
                            .forEach(requestPart -> {
                                Product product = requestPart.getProduct();
                                int amountOfOrder = requestPart.getAmount();
                                int reservedAmount = product.getReservedAmount();
                                product.setReservedAmount(reservedAmount - amountOfOrder);
                                productRepository.save(product);
                            });
                    order.setStatus(OrderStatus.REJECTED);
                    orderRepository.save(order);
                    mailSender.sendMessage(order.getClient()
                            .getEmail(),
                            "Ваш заказ отклонен!",
                            "Ваш заказ отклонен по причине: " + cancelOrder.getMessage());
                }, () -> {
                    throw new BusinessException("Заказ не найден", HttpStatus.NOT_FOUND);
                });
    }

    @Override
    public List<OrderResponse> getAllById(Integer id) {
        return orderRepository.findAllByUserId(id)
                .stream()
                .map(orderMapper::toResponseDto)
                .toList();
    }
}
