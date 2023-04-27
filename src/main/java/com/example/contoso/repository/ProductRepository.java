package com.example.contoso.repository;

import com.example.contoso.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Neevels
 * @version 1.0
 * @date 4/27/2023 5:20 PM
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Optional<Product> findByCode(Integer code);
}
