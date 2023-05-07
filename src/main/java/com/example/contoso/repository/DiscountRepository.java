package com.example.contoso.repository;

import com.example.contoso.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Neevels
 * @version 1.0
 * @date 5/7/2023 5:26 PM
 */
@Repository
public interface DiscountRepository extends JpaRepository<Discount, Integer> {
    Discount findByClientId(Integer clientId);
}
