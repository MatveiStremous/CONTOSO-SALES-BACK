package com.example.contoso.controller;

import com.example.contoso.entity.enums.StatusOfRequest;
import com.example.contoso.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Neevels
 * @version 1.0
 * @date 4/28/2023 11:39 PM
 */
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PutMapping("/{id}/{orderStatus}")
    public ResponseEntity<String> changeOrderStatus(@PathVariable Integer id,
                                                    @PathVariable StatusOfRequest orderStatus) {
        orderService.changeOrderStatus( id,  orderStatus);
        return ResponseEntity.ok("Статус успешно изменен");
    }

}
