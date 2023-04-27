package com.example.contoso.dto.mapper;

import com.example.contoso.dto.request.ProductRequest;
import com.example.contoso.dto.response.ProductResponse;
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
                .freeAmount(product.getFreeAmount())
                .reservedAmount(product.getReservedAmount())
                .price(product.getPrice())
                .name(product.getName())
                .build();
    }

    public Product toProduct(ProductRequest productRequest) {
        return Product.builder()
                .code(productRequest.getCode())
                .freeAmount(productRequest.getFreeAmount())
                .reservedAmount(productRequest.getReservedAmount())
                .name(productRequest.getName())
                .price(productRequest.getPrice())
                .build();
    }
}
