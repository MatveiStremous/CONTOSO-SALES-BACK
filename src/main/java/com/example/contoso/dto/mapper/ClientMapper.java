package com.example.contoso.dto.mapper;

import com.example.contoso.dto.request.client.ClientRequest;
import com.example.contoso.dto.response.client.ClientResponse;
import com.example.contoso.entity.Client;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Neevels
 * @version 1.0
 * @date 4/27/2023 6:11 PM
 */
@NoArgsConstructor
@Component
public class ClientMapper {
    public ClientResponse toResponseDto(Client client) {
        return ClientResponse.builder()
                .id(client.getId())
                .address(client.getAddress())
                .email(client.getEmail())
                .name(client.getName())
                .dateOfRegistration(client.getDateOfRegistration())
                .phoneNumber(client.getPhoneNumber())
                .discount(client.getDiscount().getDiscountType())
                .build();
    }

    public Client toClient(ClientRequest clientRequest, Integer id) {
        return Client.builder()
                .id(id)
                .address(clientRequest.getAddress())
                .name(clientRequest.getName())
                .email(clientRequest.getEmail())
                .phoneNumber(clientRequest.getPhoneNumber())
                .build();
    }
}
