package com.example.contoso.service.impl;

import com.example.contoso.dto.mapper.ProductMapper;
import com.example.contoso.dto.request.product.ProductRequest;
import com.example.contoso.dto.response.product.ProductResponse;
import com.example.contoso.entity.Product;
import com.example.contoso.exception.type.BusinessException;
import com.example.contoso.repository.ProductRepository;
import com.example.contoso.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
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
    private static final String ALREADY_EXIST = "Продукт с кодом %s уже существует.";
    private static final String NOT_FOUND = "Продукт с id %s не найден.";
    private static final String CODE_ALREADY_EXIST = "Продукт с кодом %s уже существует.";

    @Override
    public void addProduct(ProductRequest productRequest) {
        Optional<Product> product = productRepository.findByCode(productRequest.getCode());
        if (product.get().getCode().equals(productRequest.getCode()) && !product.get().isActive()) {
            productRepository.save(Product.builder()
                            .id(product.get().getId())
                            .isActive(true)
                            .name(productRequest.getName())
                            .price(productRequest.getPrice())
                            .reservedAmount(0)
                            .amount(product.get().getAmount())
                            .code(productRequest.getCode())
                    .build());
        }
        else if (product.isEmpty()) {
            productRepository.save(productMapper.toProduct(productRequest));
        } else {
            throw new BusinessException(String.format(ALREADY_EXIST, productRequest.getCode()),
                    HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void deleteProductById(Integer id) {
        productRepository.findById(id)
                .ifPresentOrElse(product -> {
                            if (product.getReservedAmount() > 0) {
                                throw new BusinessException("Вы не можете удалить товар, так как определенное количество находится в резерве", HttpStatus.FORBIDDEN);
                            } else {
                                product.setActive(false);
                                productRepository.save(product);
                            }
                        },
                        () -> {
                            throw new BusinessException(String.format(NOT_FOUND, id), HttpStatus.NOT_FOUND);
                        });
    }

    @Override
    public void updateProduct(ProductRequest productRequest, Integer id) {
        productRepository.findById(id)
                .ifPresentOrElse(prod -> {
                            if (productRequest.getCode()
                                    .equals(prod.getCode())) {
                                Product product1 = productMapper.toProduct(productRequest);
                                product1.setId(id);
                                productRepository.save(product1);
                            } else {
                                Optional<Product> productByCode = productRepository.findByCode(productRequest.getCode());
                                if (productByCode.isEmpty()) {
                                    Product product1 = productMapper.toProduct(productRequest);
                                    product1.setId(id);
                                    productRepository.save(product1);
                                } else {
                                    throw new BusinessException(String.format(CODE_ALREADY_EXIST, productRequest.getCode()), HttpStatus.CONFLICT);
                                }
                            }
                        },
                        () -> {
                            throw new BusinessException(String.format(NOT_FOUND, id), HttpStatus.NOT_FOUND);
                        });

    }

    @Override
    public List<ProductResponse> getAll() {
        return productRepository.findAll()
                .stream()
                .filter(product -> Objects.equals(product.isActive(), true))
                .map(productMapper::toResponseDto)
                .toList();
    }

    @Override
    public void updateProductAmount(Integer amount, Integer id) {
        productRepository.findById(id)
                .ifPresentOrElse(product -> {
                            product.setAmount(product.getAmount() + amount);
                            productRepository.save(product);
                        },
                        () -> {
                            throw new BusinessException(String.format(NOT_FOUND, id), HttpStatus.NOT_FOUND);
                        });
    }

}
