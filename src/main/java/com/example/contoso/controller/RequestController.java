package com.example.contoso.controller;

import com.example.contoso.dto.request.RequestRequest;
import com.example.contoso.dto.response.RequestResponse;
import com.example.contoso.entity.Product;
import com.example.contoso.entity.Request;
import com.example.contoso.entity.RequestPart;
import com.example.contoso.repository.ClientRepository;
import com.example.contoso.repository.ProductRepository;
import com.example.contoso.repository.RequestRepository;
import com.example.contoso.service.RequestService;
import jakarta.annotation.PostConstruct;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Neevels
 * @version 1.0
 * @date 4/27/2023 10:15 PM
 */
@RestController
@RequestMapping("/request")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;

    @PostMapping
    public ResponseEntity<String> addRequest(@RequestBody RequestRequest requestRequest) {
        requestService.addRequest(requestRequest);
        return ResponseEntity.ok("Заявка успешно создана!");
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<RequestResponse>> getById(@PathVariable Integer userId) {
        return ResponseEntity.ok(requestService.getById(userId));
    }

    @DeleteMapping("/{requestId}")
    public ResponseEntity<String> deleteById(@PathVariable Integer requestId) {
        requestService.deleteRequest(requestId);
        return ResponseEntity.ok("Успешно удален");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateById(@RequestBody RequestRequest requestRequest, @PathVariable Integer id) {
        requestService.updateRequest(requestRequest, id);
        return ResponseEntity.ok("Успешно обновлен");
    }

    @PutMapping("status/{requestId}")
    public ResponseEntity<String> updateByRequestStatus(@PathVariable Integer requestId) {
        requestService.changeStatusToCancelled(requestId);
        return ResponseEntity.ok("Успешно обновлен");
    }
}
