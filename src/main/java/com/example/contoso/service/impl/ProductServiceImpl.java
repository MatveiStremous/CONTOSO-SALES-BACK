package com.example.contoso.service.impl;

import com.example.contoso.dto.mapper.ProductMapper;
import com.example.contoso.dto.request.ProductRequest;
import com.example.contoso.dto.response.ProductResponse;
import com.example.contoso.entity.Product;
import com.example.contoso.exception.type.BusinessException;
import com.example.contoso.repository.ProductRepository;
import com.example.contoso.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Neevels
 * @version 1.0
 * @date 4/27/2023 5:24 PM
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;

    @Override
    public void addProduct(ProductRequest productRequest) {
        Optional<Product> product = productRepository.findByCode(productRequest.getCode());
        if (product.isEmpty()) {
            productRepository.save(productMapper.toProduct(productRequest));
        } else {
            throw new BusinessException(String.format("Продукт с кодом: %s уже существует.", productRequest.getCode()),
                    HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void deleteProductById(Integer id) {
        productRepository.findById(id)
                .ifPresentOrElse(productRepository::delete,
                        () -> {
                            throw new BusinessException("Продукт не найден!", HttpStatus.NOT_FOUND);
                        });
    }

    @Override
    public void updateProduct(ProductRequest productRequest, Integer id) {
        if (productRepository.findByCode(productRequest.getCode()).isEmpty()) {
            productRepository.findById(id)
                    .ifPresentOrElse(product -> {
                                Product product1 = productMapper.toProduct(productRequest);
                                product1.setId(id);
                                productRepository.save(product1);
                            },
                            () -> {
                                throw new BusinessException("Продукт не найден!", HttpStatus.NOT_FOUND);
                            });
        } else {
            throw new BusinessException(String.format("Продукт с кодом: %s уже существует.", productRequest.getCode()),
                    HttpStatus.CONFLICT);
        }

    }

    @Override
    public List<ProductResponse> getAll() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toResponseDto)
                .toList();
    }
}
