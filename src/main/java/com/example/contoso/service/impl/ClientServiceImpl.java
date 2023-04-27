package com.example.contoso.service.impl;

import com.example.contoso.utils.MailSender;
import com.example.contoso.dto.mapper.ClientMapper;
import com.example.contoso.dto.request.ClientRequest;
import com.example.contoso.dto.response.ClientResponse;
import com.example.contoso.entity.Client;
import com.example.contoso.exception.type.BusinessException;
import com.example.contoso.repository.ClientRepository;
import com.example.contoso.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Neevels
 * @version 1.0
 * @date 4/27/2023 6:15 PM
 */
@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final MailSender mailSender;

    @Override
    public void addClient(ClientRequest clientRequest) {
        Optional<Client> client = clientRepository.findByEmail(clientRequest.getEmail());
        if (client.isEmpty()) {
            clientRepository.save(clientMapper.toClient(clientRequest, null));
        } else {
            throw new BusinessException(String.format("Клиент с почтой: %s уже существует.", clientRequest.getEmail()),
                    HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void deleteClient(Integer id) {
        clientRepository.findById(id)
                .ifPresentOrElse(clientRepository::delete,
                        () -> {
                            throw new BusinessException("Пользователь не найден!", HttpStatus.NOT_FOUND);
                        });
    }

    @Override
    public void updateClient(ClientRequest clientRequest, Integer id) {
        clientRepository.findById(id)
                .ifPresentOrElse(client -> {
                            clientRepository.save(clientMapper.toClient(clientRequest, id));
                        },
                        () -> {
                            throw new BusinessException("Клиент не найден!", HttpStatus.NOT_FOUND);
                        });

    }

    @Override
    public List<ClientResponse> getAll() {
        return clientRepository.findAll()
                .stream()
                .map(clientMapper::toResponseDto)
                .toList();
    }

    @Override
    public void sendMessageById(String message, Integer id) {
        clientRepository.findById(id)
                .ifPresentOrElse(client -> {
                            sendMessageToEmail(client.getEmail(), message);
                        }, () -> {
                            throw new BusinessException("Клиент не найден!", HttpStatus.NOT_FOUND);
                        }
                );

    }

    @Override
    public void sendMessageToAll(String message) {
        clientRepository.findAll()
                .forEach(client -> sendMessageToEmail(client.getEmail(), message));
    }


    private void sendMessageToEmail(String email, String message) {
        mailSender.send(email, "Hi", message);
    }

}
