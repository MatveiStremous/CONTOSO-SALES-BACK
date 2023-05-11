package com.example.contoso.service;

import com.example.contoso.dto.request.product.ProductRequest;
import com.example.contoso.dto.response.product.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

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

    void updateProductAmount(Integer amount, Integer id);

    void updateProductAmountViaExcelFile(MultipartFile file);

}
