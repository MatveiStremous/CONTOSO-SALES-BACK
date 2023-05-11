package com.example.contoso.service.impl;

import com.example.contoso.dto.mapper.ProductMapper;
import com.example.contoso.dto.request.product.ProductRequest;
import com.example.contoso.dto.response.product.ProductResponse;
import com.example.contoso.entity.Product;
import com.example.contoso.exception.type.BusinessException;
import com.example.contoso.repository.ProductRepository;
import com.example.contoso.service.ProductService;
import com.example.contoso.utils.FileData;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

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
        productRepository.findByCode(productRequest.getCode()).ifPresentOrElse(product -> {
            if (product.getCode().equals(productRequest.getCode()) && !product.isActive()) {
                productRepository.save(Product.builder()
                        .id(product.getId())
                        .isActive(true)
                        .name(productRequest.getName())
                        .price(productRequest.getPrice())
                        .reservedAmount(0)
                        .amount(product.getAmount())
                        .code(productRequest.getCode())
                        .build());
            } else {
                throw new BusinessException(String.format(ALREADY_EXIST, productRequest.getCode()),
                        HttpStatus.NOT_FOUND);
            }
        } , () -> {
            productRepository.save(productMapper.toProduct(productRequest));
        });
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

    @Override
    public void updateProductAmountViaExcelFile(MultipartFile file) {
        Workbook workbook = null;
        List<FileData> fileData = new ArrayList<>();
        try {
            workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                Cell firstCell = row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                Cell secondCell = row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                fileData.add(FileData.builder()
                        .code(firstCell.getStringCellValue())
                        .amount((int) secondCell.getNumericCellValue())
                        .build());
            }
            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        updateAllProducts(fileData);
    }

    private void updateAllProducts(List<FileData> fileData) {
        List<String> articles = new ArrayList<>();

        fileData
                .forEach(data -> {
                    productRepository.findByCode(data.getCode())
                            .ifPresentOrElse(item -> {
                                        item.setAmount(item.getAmount() + data.getAmount());
                                        productRepository.save(item);
                                    },
                                    () -> articles.add(data.getCode())
                            );

                });
        if (!articles.isEmpty()) {
            throw new BusinessException("Товаров со следующими артикулами: " + articles + " не существует!",
                    HttpStatus.NOT_IMPLEMENTED);
        }
    }

}
