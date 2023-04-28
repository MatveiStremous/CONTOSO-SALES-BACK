package com.example.contoso.service;

import com.example.contoso.dto.request.client.ClientRequest;
import com.example.contoso.dto.request.client.MessageRequest;
import com.example.contoso.dto.response.client.ClientResponse;
import org.springframework.web.multipart.MultipartFile;

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

    void sendMessageToAll(MessageRequest messageRequest, MultipartFile file);

    void sendMessageById(MessageRequest messageRequest, Integer id, MultipartFile file);
}
