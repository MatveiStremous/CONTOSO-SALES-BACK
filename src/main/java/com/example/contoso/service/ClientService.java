package com.example.contoso.service;

import com.example.contoso.dto.request.ClientRequest;
import com.example.contoso.dto.response.ClientResponse;

import java.util.List;

/**
 * @author Neevels
 * @version 1.0
 * @date 4/27/2023 6:13 PM
 */
public interface ClientService {
    void addClient(ClientRequest clientRequest);
    void deleteClient(Integer id);
    void updateClient(ClientRequest clientRequest, Integer id);
    List<ClientResponse> getAll();
    void sendMessageById(String message, Integer id);
    void sendMessageToAll(String message);

}
