package com.example.contoso.controller;

import com.example.contoso.dto.request.user.AmountRequest;
import com.example.contoso.dto.request.product.ProductRequest;
import com.example.contoso.dto.response.product.ProductResponse;
import com.example.contoso.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Neevels
 * @version 1.0
 * @date 4/27/2023 5:16 PM
 */
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<String> addProduct(@RequestBody ProductRequest productRequest) {
        productService.addProduct(productRequest);
        return ResponseEntity.ok("Продукт успешно добавлен!");
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAll() {
        return ResponseEntity
                .ok()
                .body(productService.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Integer id) {
        productService.deleteProductById(id);
        return ResponseEntity.ok("Продукт успешно удален!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(@RequestBody ProductRequest productRequest,
                                                @PathVariable Integer id) {
        productService.updateProduct(productRequest, id);
        return ResponseEntity.ok("Продукт успешно обновлен!");
    }

    @PutMapping("amount")
    public ResponseEntity<String> updateProduct(@RequestBody AmountRequest amountRequest) {
        productService.updateProductAmount(amountRequest.getAmount(), amountRequest.getProductId());
        return ResponseEntity.ok("Продукт успешно обновлен!");
    }


}
