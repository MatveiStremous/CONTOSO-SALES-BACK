package com.example.contoso.service;

import com.example.contoso.dto.request.ProductRequest;
import com.example.contoso.dto.response.ProductResponse;

import java.util.List;

/**
 * @author Neevels
 * @version 1.0
 * @date 4/27/2023 5:20 PM
 */
public interface ProductService {
    void addProduct(ProductRequest productRequest);
    void deleteProductById(Integer id);
    void updateProduct(ProductRequest productRequest, Integer id);
    List<ProductResponse> getAll();
}
