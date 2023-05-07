package com.example.contoso.repository;

import com.example.contoso.dto.response.order.OrderResponse;
import com.example.contoso.entity.Client;
import com.example.contoso.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Neevels
 * @version 1.0
 * @date 4/28/2023 11:38 PM
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findAllByClient(Client client);
    List<Order> findAllByClosingDate(LocalDate closingDate);
    List<Order> findAllByClosingDateBetween(LocalDate from, LocalDate to);
    List<Order> findAllByUserId(Integer userId);
}
