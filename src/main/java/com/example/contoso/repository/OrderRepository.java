package com.example.contoso.repository;

import com.example.contoso.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Neevels
 * @version 1.0
 * @date 4/28/2023 11:38 PM
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
}
