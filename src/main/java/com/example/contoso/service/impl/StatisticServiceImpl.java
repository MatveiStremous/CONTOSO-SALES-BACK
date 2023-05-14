package com.example.contoso.service.impl;

import com.example.contoso.dto.response.statistic.FailedSuccessResponse;
import com.example.contoso.dto.response.statistic.MostActiveBuyersResponse;
import com.example.contoso.dto.response.statistic.MostPopularItemResponse;
import com.example.contoso.dto.response.statistic.ProfitResponse;
import com.example.contoso.entity.Order;
import com.example.contoso.entity.enums.OrderStatus;
import com.example.contoso.exception.type.BusinessException;
import com.example.contoso.repository.OrderRepository;
import com.example.contoso.repository.ProductRepository;
import com.example.contoso.service.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Neevels
 * @version 1.0
 * @date 5/7/2023 7:11 PM
 */
@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Override
    public List<ProfitResponse> getProfitFor(LocalDate from, LocalDate to) {
        checkDate(from, to);
        return from.datesUntil(to.plusDays(1))
                .map(localDate -> {
                    double sumPerDay = orderRepository.findAllByClosingDate(localDate)
                            .stream()
                            .filter(order -> Objects.equals(order.getStatus(), OrderStatus.COMPLETED))
                            .mapToDouble(Order::getFinalPrice)
                            .sum();
                    return ProfitResponse.builder()
                            .key(localDate)
                            .value(sumPerDay)
                            .build();
                })
                .toList();
    }

    @Override
    public List<FailedSuccessResponse> getFailedSuccessOrders(LocalDate from, LocalDate to) {
        checkDate(from, to);
        List<Order> allByClosingDateBetween = orderRepository.findAllByClosingDateBetween(from, to);
        return Stream.of(OrderStatus.COMPLETED, OrderStatus.REJECTED)
                .map(orderStatus -> {
                            Integer count = (int) allByClosingDateBetween.stream()
                                    .filter(order -> order.getStatus()
                                            .equals(orderStatus))
                                    .count();
                            return FailedSuccessResponse.builder()
                                    .key(orderStatus.getUrl())
                                    .value(count)
                                    .build();
                        }
                )
                .toList();
    }

    @Override
    public List<MostActiveBuyersResponse> getTheMostActiveBuyers() {
        final int MAX_ACTIVE_BUYERS = 10;
        return orderRepository.findAll()
                .stream()
                .filter(order -> Objects.equals(order.getStatus(), OrderStatus.COMPLETED))
                .collect(Collectors.groupingBy(order -> order.getClient()
                        .getEmail(), Collectors.counting()))
                .entrySet()
                .stream()
                .map(stringLongEntry -> MostActiveBuyersResponse.builder()
                        .key(stringLongEntry.getKey())
                        .value(stringLongEntry.getValue())
                        .build())
                .limit(MAX_ACTIVE_BUYERS)
                .sorted(Comparator.comparing(MostActiveBuyersResponse::value))
                .toList();
    }

    @Override
    public List<MostPopularItemResponse> getTheMostPopularItem() {
        final int MAX_ITEMS = 10;
        return orderRepository.findAll()
                .stream()
                .filter(order -> order.getStatus()
                        .equals(OrderStatus.COMPLETED))
                .map(Order::getListRequest)
                .map(requestParts -> requestParts
                        .stream()
                        .collect(Collectors.groupingBy(requestPart -> productRepository.findById(requestPart.getProductId()).get().getName(), Collectors.counting()))
                        .entrySet()
                        .stream()
                        .map(stringLongEntry -> MostPopularItemResponse.builder()
                                .key(stringLongEntry.getKey())
                                .value(stringLongEntry.getValue())
                                .build())
                        .limit(MAX_ITEMS)
                        .toList())
                .flatMap(Collection::stream)
                .collect(Collectors.groupingBy(MostPopularItemResponse::key, Collectors.counting()))
                .entrySet()
                .stream()
                .map(stringLongEntry -> MostPopularItemResponse.builder()
                        .key(stringLongEntry.getKey())
                        .value(stringLongEntry.getValue())
                        .build())
                .sorted(Comparator.comparing(MostPopularItemResponse::value))
                .toList();
    }


    private void checkDate(LocalDate from, LocalDate to) {
        if (from.isAfter(to)) {
            throw new BusinessException("Ошибка! Попробуйте поставить правильный диапазон!", HttpStatus.BAD_REQUEST);
        }
    }
}
