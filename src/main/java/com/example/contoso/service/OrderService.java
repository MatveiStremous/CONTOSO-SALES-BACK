package com.example.contoso.service;

import com.example.contoso.entity.enums.StatusOfRequest;

/**
 * @author Neevels
 * @version 1.0
 * @date 4/28/2023 11:38 PM
 */
public interface OrderService {
    void changeOrderStatus(Integer id, StatusOfRequest orderStatus);
}
