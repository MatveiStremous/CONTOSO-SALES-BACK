package com.example.contoso.service.impl;

import com.example.contoso.dto.request.RequestRequest;
import com.example.contoso.dto.response.R;
import com.example.contoso.dto.response.RequestResponse;
import com.example.contoso.entity.*;
import com.example.contoso.exception.type.BusinessException;
import com.example.contoso.repository.ClientRepository;
import com.example.contoso.repository.ProductRepository;
import com.example.contoso.repository.RequestRepository;
import com.example.contoso.repository.UserRepository;
import com.example.contoso.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Neevels
 * @version 1.0
 * @date 4/27/2023 10:16 PM
 */
@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private static final String NOT_FOUND = "Продукт с id %s не найден.";
    private static final String CLIENT_NOT_FOUND = "Клиент с id %s не найден.";
    private static final String MANAGER_NOT_FOUND = "Менеджер с id %s не найден.";
    private static final String CODE_NOT_FOUND = "Артикул с id %s не найден.";

    @Override
    public void addRequest(RequestRequest requestRequest) {
        List<RequestPart> requestLists = requestRequest.getRequestLists()
                .stream()
                .map(requestRequestList -> {
                    Optional<Product> product = productRepository.findById(requestRequestList.getProductId());
                    if (product.isPresent()) {
                        return RequestPart.builder()
                                .product(product.get())
                                .amount(requestRequestList.getAmount())
                                .build();
                    } else {
                        throw new BusinessException(String.format(NOT_FOUND, requestRequestList.getProductId()), HttpStatus.NOT_FOUND);
                    }

                })
                .toList();
        Optional<User> user = userRepository.findById(requestRequest.getUserId());
        Optional<Client> client = clientRepository.findById(requestRequest.getClientId());
        if (client.isPresent() && user.isPresent()) {
            Request build = Request.builder()
                    .status(Request.StatusOfRequest.DECORATED)
                    .user(user.get())
                    .dateOfDelivery(requestRequest.getDateOfDelivery())
                    .note(requestRequest.getNote())
                    .listRequest(requestLists)
                    .client(client.get())
                    .build();
            requestRepository.save(build);
        } else if (client.isEmpty()) {
            throw new BusinessException(String.format(CLIENT_NOT_FOUND, requestRequest.getClientId()), HttpStatus.NOT_FOUND);
        } else {
            throw new BusinessException(String.format(MANAGER_NOT_FOUND, requestRequest.getUserId()), HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void updateRequest(RequestRequest requestRequest, Integer id) {
        requestRepository.findById(id)
                .ifPresentOrElse(request -> {
                    List<RequestPart> requestParts = requestRequest.getRequestLists()
                            .stream()
                            .map(requestPart -> {
                                Optional<Product> product = productRepository.findById(requestPart.getProductId());
                                return RequestPart.builder()
                                        .amount(requestPart.getAmount())
                                        .product(product.get())
                                        .build();
                            })
                            .toList();
                    Optional<User> user = userRepository.findById(requestRequest.getUserId());
                    Optional<Client> client = clientRepository.findById(requestRequest.getClientId());
                    Request build = Request.builder()
                            .id(request.getId())
                            .status(Request.StatusOfRequest.DECORATED)
                            .user(user.get())
                            .dateOfDelivery(requestRequest.getDateOfDelivery())
                            .note(requestRequest.getNote())
                            .time(request.getTime())
                            .listRequest(requestParts)
                            .client(client.get())
                            .note(requestRequest.getNote())
                            .build();
                    requestRepository.save(build);
                }, () -> {
                    throw new BusinessException(String.format(CODE_NOT_FOUND, id), HttpStatus.NOT_FOUND);
                });
    }

    @Override
    public void deleteRequest(Integer id) {
        requestRepository.deleteById(id);
    }

    @Override
    public void changeStatusToCancelled(Integer requestId) {
        requestRepository.findById(requestId)
                .ifPresentOrElse(request -> {
                            request.setStatus(Request.StatusOfRequest.CANCELLED);
                            requestRepository.save(request);
                        }, () -> {
                            throw new BusinessException(String.format(CODE_NOT_FOUND, requestId), HttpStatus.NOT_FOUND);

                        }
                );
    }

    @Override
    public List<RequestResponse> getById(Integer id) {
        return requestRepository.findAllByUserId(id)
                .stream()
                .map(request ->
                        RequestResponse.builder()
                                .requestId(request.getId())
                                .clientEmail(request.getClient()
                                        .getEmail())
                                .status(request.getStatus()
                                        .getUrl())
                                .dateTime(request.getTime())
                                .dateOfDelivery(request.getDateOfDelivery())
                                .note(request.getNote())
                                .rList(request.getListRequest()
                                        .stream()
                                        .map(requestPart ->
                                                R.builder()
                                                        .reservedAmount(requestPart.getProduct().getReservedAmount())
                                                        .amount(requestPart.getProduct().getAmount())
                                                        .pricePerItem(requestPart.getProduct().getPrice())
                                                        .clientAmount(requestPart.getAmount())
                                                        .code(requestPart.getProduct().getCode())
                                                        .name(requestPart.getProduct().getName())
                                                        .productId(requestPart.getProduct().getId())
                                                        .build()
                                        )
                                        .toList())
                                .build()
                )
                .toList();
    }

    @Override
    public void changeStatus(Integer requestId, Request.StatusOfRequest status) {
        requestRepository.findById(requestId)
                .ifPresentOrElse(request -> {
                            request.setStatus(status);
                            requestRepository.save(request);
                        }, () -> {
                            throw new BusinessException("Заявка не найдена", HttpStatus.NOT_FOUND);
                        }
                );
    }
}
