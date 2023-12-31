package com.example.contoso.controller;

import com.example.contoso.dto.request.order.CancelOrderRequest;
import com.example.contoso.dto.request.user.UserRequest;
import com.example.contoso.dto.response.statistic.FailedSuccessResponse;
import com.example.contoso.dto.response.statistic.MostActiveBuyersResponse;
import com.example.contoso.dto.response.statistic.MostPopularItemResponse;
import com.example.contoso.dto.response.statistic.ProfitResponse;
import com.example.contoso.dto.response.request.RequestResponse;
import com.example.contoso.dto.response.user.UserResponse;
import com.example.contoso.entity.enums.StatusOfRequest;
import com.example.contoso.service.OrderService;
import com.example.contoso.service.RequestService;
import com.example.contoso.service.StatisticService;
import com.example.contoso.service.UserService;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * @author Neevels
 * @version 1.0
 * @date 4/27/2023 11:23 AM
 */
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    private final RequestService requestService;
    private final OrderService orderService;
    private final StatisticService statisticService;

    @PostMapping("/registration")
    public ResponseEntity<String> register(@Valid @RequestBody UserRequest userRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(bindingResult.getAllErrors()
                            .get(0)
                            .getDefaultMessage());
        }
        userService.registration(userRequest);
        return ResponseEntity.ok()
                .body("User successfully registered");
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAll() {
        return ResponseEntity
                .ok()
                .body(userService.getAllManagers());
    }

    @GetMapping("/requests")
    public ResponseEntity<List<RequestResponse>> getAllRequests() {
        return ResponseEntity
                .ok()
                .body(requestService.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Integer id) {
        userService.deleteById(id);
        return ResponseEntity.ok("Успешно удален");
    }

    @PutMapping("status/{requestId}/{status}")
    public ResponseEntity<String> updateByRequestStatus(@PathVariable Integer requestId,
                                                        @PathVariable StatusOfRequest status) {
        requestService.changeStatus(requestId, status);
        return ResponseEntity.ok("Успешно обновлен");
    }

    @PutMapping("order")
    public ResponseEntity<String> updateByRequestStatus(@RequestBody CancelOrderRequest cancelOrder) {
        orderService.cancelOrder(cancelOrder);
        return ResponseEntity.ok("Успешно отклонен");
    }

    @GetMapping("profit/{from}/{to}")
    public ResponseEntity<List<ProfitResponse>> getProfitFor(@PathVariable @JsonFormat(pattern = "yyyy-MM-dd") LocalDate from,
                                                       @PathVariable @JsonFormat(pattern = "yyyy-MM-dd") LocalDate to) {
        return ResponseEntity.ok()
                .body(statisticService.getProfitFor(from, to));
    }

    @GetMapping("failed-success/{from}/{to}")
    public ResponseEntity<List<FailedSuccessResponse>> getFailedSuccessOrders(@PathVariable @JsonFormat(pattern = "yyyy-MM-dd") LocalDate from,
                                                                              @PathVariable @JsonFormat(pattern = "yyyy-MM-dd") LocalDate to) {
        return ResponseEntity.ok()
                .body(statisticService.getFailedSuccessOrders(from, to));
    }

    @GetMapping("most-active-buyers")
    public ResponseEntity<List<MostActiveBuyersResponse>> getTheMostActiveBuyers() {
        return ResponseEntity.ok()
                .body(statisticService.getTheMostActiveBuyers());
    }

    @GetMapping("most-popular-items")
    public ResponseEntity<List<MostPopularItemResponse>> getTheMostPopularItem() {
        return ResponseEntity.ok()
                .body(statisticService.getTheMostPopularItem());
    }
}
