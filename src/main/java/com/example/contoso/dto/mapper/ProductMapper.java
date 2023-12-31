package com.example.contoso.dto.mapper;

import com.example.contoso.dto.request.product.ProductRequest;
import com.example.contoso.dto.response.product.ProductResponse;
import com.example.contoso.entity.Product;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Neevels
 * @version 1.0
 * @date 4/27/2023 5:25 PM
 */
@NoArgsConstructor
@Component
public class ProductMapper {
    public ProductResponse toResponseDto(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .code(product.getCode())
                .amount(product.getAmount())
                .reservedAmount(product.getReservedAmount())
                .price(product.getPrice())
                .name(product.getName())
                .build();
    }

    public Product toProduct(ProductRequest productRequest) {
        return Product.builder()
                .code(productRequest.getCode())
                .amount(productRequest.getAmount())
                .reservedAmount(productRequest.getReservedAmount())
                .name(productRequest.getName())
                .price(productRequest.getPrice())
                .isActive(true)
                .build();
    }
}
