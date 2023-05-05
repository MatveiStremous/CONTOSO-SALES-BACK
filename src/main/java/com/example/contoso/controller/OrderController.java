package com.example.contoso.controller;

import com.example.contoso.dto.response.order.OrderResponse;
import com.example.contoso.entity.enums.OrderStatus;
import com.example.contoso.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
                                                    @PathVariable OrderStatus orderStatus) {
        orderService.changeOrderStatus(id, orderStatus);
        return ResponseEntity.ok("Статус успешно изменен");
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> changeOrderStatus() {
        return ResponseEntity.ok()
                .body(orderService.getAll());
    }

}
