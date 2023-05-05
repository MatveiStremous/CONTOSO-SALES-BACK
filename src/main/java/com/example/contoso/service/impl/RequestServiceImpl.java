package com.example.contoso.service.impl;

import com.example.contoso.dto.request.request.RequestRequest;
import com.example.contoso.dto.response.MailResponse;
import com.example.contoso.dto.response.request.R;
import com.example.contoso.dto.response.request.RequestResponse;
import com.example.contoso.entity.*;
import com.example.contoso.entity.enums.StatusOfRequest;
import com.example.contoso.exception.type.BusinessException;
import com.example.contoso.repository.*;
import com.example.contoso.service.RequestService;
import com.example.contoso.utils.MailSender;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Neevels
 * @version 1.0
 * @date 4/27/2023 10:16 PM
 */
@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    //TODO когда статус заказа закрыт то поле с резервным продуктом обноляется

    private final OrderRepository orderRepository;
    private final RequestRepository requestRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final MailSender mailSender;

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
                    .status(StatusOfRequest.DECORATED)
                    .user(user.get())
                    .dateOfDelivery(requestRequest.getDateOfDelivery())
                    .note(requestRequest.getNote())
                    .listRequest(requestLists)
                    .paymentMethod(requestRequest.getPaymentMethod())
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
                            .status(StatusOfRequest.DECORATED)
                            .user(user.get())
                            .dateOfDelivery(requestRequest.getDateOfDelivery())
                            .note(requestRequest.getNote())
                            .time(request.getTime())
                            .paymentMethod(request.getPaymentMethod())
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
                            request.setStatus(StatusOfRequest.CANCELLED);
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
                                .paymentMethod(request.getPaymentMethod())
                                .rList(request.getListRequest()
                                        .stream()
                                        .map(requestPart ->
                                                R.builder()
                                                        .reservedAmount(requestPart.getProduct()
                                                                .getReservedAmount())
                                                        .amount(requestPart.getProduct()
                                                                .getAmount())
                                                        .pricePerItem(requestPart.getProduct()
                                                                .getPrice())
                                                        .clientAmount(requestPart.getAmount())
                                                        .code(requestPart.getProduct()
                                                                .getCode())
                                                        .name(requestPart.getProduct()
                                                                .getName())
                                                        .productId(requestPart.getProduct()
                                                                .getId())
                                                        .build()
                                        )
                                        .toList())
                                .build()
                )
                .toList();
    }

    @Transactional
    @Override
    public void changeStatus(Integer requestId, StatusOfRequest status) {
        requestRepository.findById(requestId)
                .ifPresentOrElse(request -> {
                            if (Objects.equals(status, StatusOfRequest.DECORATED)) {
                                Order order = Order.builder()
                                        .status(StatusOfRequest.DECORATED)
                                        .user(request.getUser())
                                        .listRequest(request.getListRequest())
                                        .paymentMethod(request.getPaymentMethod())
                                        .client(request.getClient())
                                        .build();
                                request.getListRequest()
                                        .forEach(requestPart -> {
                                            /**
                                             * Manipulation with data before creating order
                                             */
                                            Product product = requestPart.getProduct();
                                            if (product.getAmount() - requestPart.getAmount() >= 0) {
                                                product.setReservedAmount(requestPart.getAmount());
                                                productRepository.save(product);
                                            } else {
                                                throw new BusinessException("Количество единиц продукта на складе меньше, чем требуется в заказе. Попробуйте пополнить склад для проведения операции",
                                                        HttpStatus.FORBIDDEN);
                                            }

                                        });
                                List<MailResponse> mailResponses = request.getListRequest()
                                        .stream()
                                        .map(requestPart -> MailResponse.builder()
                                                .productAmount(requestPart.getAmount())
                                                .productName(requestPart.getProduct()
                                                        .getName())
                                                .price(requestPart.getAmount() * requestPart.getProduct()
                                                        .getPrice())
                                                .build())
                                        .toList();
                                Double price = request.getListRequest()
                                        .stream()
                                        .mapToDouble(requestPart -> requestPart.getAmount() * requestPart.getProduct()
                                                .getPrice())
                                        .sum();
                                order.setFinalPrice(price);
                                mailSender.send(request.getClient()
                                        .getEmail(), "Заказ", "Благодарим вас за сделанный вами заказ. Оставайтесь с нами", mailResponses, price, request.getPaymentMethod());
                                orderRepository.save(order);
                                requestRepository.delete(request);
                            } else {
                                request.setStatus(status);
                                requestRepository.save(request);
                            }
                        }, () -> {
                            throw new BusinessException("Заявка не найдена", HttpStatus.NOT_FOUND);
                        }
                );
    }

    @Override
    public List<RequestResponse> getAll() {
        return requestRepository.findAll()
                .stream()
                .map(request -> RequestResponse.builder()
                        .requestId(request.getId())
                        .clientEmail(request.getClient()
                                .getEmail())
                        .status(request.getStatus()
                                .getUrl())
                        .dateTime(request.getTime())
                        .dateOfDelivery(request.getDateOfDelivery())
                        .note(request.getNote())
                        .paymentMethod(request.getPaymentMethod())
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
                        .build())
                .toList();
    }
}
