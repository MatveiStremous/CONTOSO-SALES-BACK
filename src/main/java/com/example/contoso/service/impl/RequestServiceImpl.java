package com.example.contoso.service.impl;

import com.example.contoso.dto.mapper.OrderMapper;
import com.example.contoso.dto.mapper.RequestMapper;
import com.example.contoso.dto.request.request.RequestRequest;
import com.example.contoso.dto.response.MailResponse;
import com.example.contoso.dto.response.request.RequestResponse;
import com.example.contoso.entity.*;
import com.example.contoso.entity.enums.OrderStatus;
import com.example.contoso.entity.enums.StatusOfRequest;
import com.example.contoso.exception.type.BusinessException;
import com.example.contoso.repository.*;
import com.example.contoso.service.DiscountService;
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

    private final OrderRepository orderRepository;
    private final RequestRepository requestRepository;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final MailSender mailSender;
    private final DiscountService discountService;
    private final OrderMapper orderMapper;
    private final RequestMapper requestMapper;

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
            Request request = requestMapper.toRequest(requestRequest, user.get(), client.get(), StatusOfRequest.DECORATED, requestLists);
            requestRepository.save(request);
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
                                Product product = productRepository.findById(requestPart.getProductId())
                                        .orElseThrow(() -> new BusinessException(CODE_NOT_FOUND, HttpStatus.NOT_FOUND));
                                return RequestPart.builder()
                                        .amount(requestPart.getAmount())
                                        .product(product)
                                        .build();
                            })
                            .toList();
                    User user = userRepository.findById(requestRequest.getUserId())
                            .orElseThrow(() -> new BusinessException(MANAGER_NOT_FOUND, HttpStatus.NOT_FOUND));
                    Request buildRequest = Request.builder()
                            .id(request.getId())
                            .status(StatusOfRequest.DECORATED)
                            .user(user)
                            .dateOfDelivery(requestRequest.getDateOfDelivery())
                            .note(requestRequest.getNote())
                            .time(request.getTime())
                            .paymentMethod(requestRequest.getPaymentMethod())
                            .listRequest(requestParts)
                            .client(request.getClient())
                            .note(requestRequest.getNote())
                            .build();
                    requestRepository.save(buildRequest);
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
                .map(requestMapper::toResponseDto)
                .toList();
    }

    @Transactional
    @Override
    public void changeStatus(Integer requestId, StatusOfRequest status) {
        requestRepository.findById(requestId)
                .ifPresentOrElse(request -> {
                            if (Objects.equals(status, StatusOfRequest.COMPLETED)) {
                                mailSender.sendMessage(request.getUser().getLogin(),
                                        "Заявка успешно подтверждена",
                                        "Дорогой менеджер, поздравляем! Заявка с номером "
                                                + request.getId()
                                                + " успешно перешла в стадию закаказа. Благодарим вас за проделанную работу!"
                                );
                                Order order = orderMapper.toOrder(request, OrderStatus.DECORATED);

                                request.getListRequest()
                                        .forEach(requestPart -> {
                                            /**
                                             * Manipulation with data before creating order
                                             */
                                            Product product = requestPart.getProduct();
                                            if (product.getAmount() - requestPart.getAmount() >= 0) {
                                                product.setReservedAmount(requestPart.getAmount() + product.getReservedAmount());
                                                productRepository.save(product);
                                            } else {
                                                throw new BusinessException("Количество единиц продукта на складе меньше, чем требуется в заказе. Попробуйте пополнить склад для проведения операции",
                                                        HttpStatus.FORBIDDEN);
                                            }
                                        });

                                Integer discount = discountService.getDiscount(request.getClient().getId());
                                Double price = getPrice(request);
                                Double finalPrice = getFinalPrice(discount, price);
                                order.setFinalPrice(finalPrice);
                                fillMessage(request, finalPrice, discount, price);
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
                .filter(request -> request.getStatus()
                        .equals(StatusOfRequest.DECORATED))
                .map(requestMapper::toResponseDto)
                .toList();
    }


    private void fillMessage(Request request, Double finalPrice, Integer discount, Double price) {
        mailSender.sendOrderInformationToClient(request.getClient()
                .getEmail(),
                "Заказ",
                "Благодарим вас за сделанный вами заказ. Оставайтесь с нами",
                getMailResponses(request),
                finalPrice,
                price,
                discount,
                request.getPaymentMethod()
        );
    }

    private  List<MailResponse> getMailResponses(Request request) {
        return request.getListRequest()
                .stream()
                .map(requestPart -> MailResponse.builder()
                        .productAmount(requestPart.getAmount())
                        .productName(requestPart.getProduct().getName())
                        .price(requestPart.getAmount() * requestPart.getProduct().getPrice())
                        .build())
                .toList();
    }

    private Double getFinalPrice(Integer discount, Double price) {
        return price - (price * discount / 100);
    }

    private Double getPrice(Request request) {
        return request.getListRequest()
                .stream()
                .mapToDouble(requestPart -> requestPart.getAmount() * requestPart.getProduct().getPrice())
                .sum();
    }

}
