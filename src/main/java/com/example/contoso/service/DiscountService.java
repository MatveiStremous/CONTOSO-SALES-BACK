package com.example.contoso.service;

/**
 * @author Neevels
 * @version 1.0
 * @date 5/7/2023 5:36 PM
 */
public interface DiscountService {
    void updateDiscount(Integer clientId);
    Integer getDiscount(Integer clientId);
}
