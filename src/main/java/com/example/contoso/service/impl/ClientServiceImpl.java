package com.example.contoso.service.impl;

import com.example.contoso.dto.request.client.MessageRequest;
import com.example.contoso.utils.MailSender;
import com.example.contoso.dto.mapper.ClientMapper;
import com.example.contoso.dto.request.client.ClientRequest;
import com.example.contoso.dto.response.client.ClientResponse;
import com.example.contoso.entity.Client;
import com.example.contoso.exception.type.BusinessException;
import com.example.contoso.repository.ClientRepository;
import com.example.contoso.service.ClientService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private static final String ALREADY_EXIST = "Клиент с почтой: %s уже существует.";
    private static final String NOT_FOUND = "Пользователь с id %s не найден.";

    @Override
    public void addClient(ClientRequest clientRequest) {
        Optional<Client> client = clientRepository.findByEmail(clientRequest.getEmail());
        if (client.isEmpty()) {
            clientRepository.save(clientMapper.toClient(clientRequest, null));
        } else {
            throw new BusinessException(String.format(ALREADY_EXIST, clientRequest.getEmail()),
                    HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public void deleteClient(Integer id) {
        clientRepository.findById(id)
                .ifPresentOrElse(clientRepository::delete,
                        () -> {
                            throw new BusinessException(String.format(NOT_FOUND, id), HttpStatus.NOT_FOUND);
                        });
    }

    @Override
    public void updateClient(ClientRequest clientRequest, Integer id) {
        clientRepository.findById(id)
                .ifPresentOrElse(client -> {
                            clientRepository.save(clientMapper.toClient(clientRequest, id));
                        },
                        () -> {
                            throw new BusinessException(String.format(NOT_FOUND, id), HttpStatus.NOT_FOUND);
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
    public void sendMessageToAll(MessageRequest messageRequest, MultipartFile file) {
        clientRepository.findAll()
                .forEach(client -> sendMessageToEmail(client.getEmail(), messageRequest.getMessage(), file, messageRequest.getSubject()));
    }

    @Override
    public void sendMessageById(MessageRequest messageRequest, Integer id, MultipartFile file) {
        clientRepository.findById(id)
                .ifPresentOrElse(client -> {
                            sendMessageToEmail(client.getEmail(), messageRequest.getMessage(), file, messageRequest.getSubject());
                        }, () -> {
                            throw new BusinessException(String.format(NOT_FOUND, id), HttpStatus.NOT_FOUND);
                        }
                );
    }

    private void sendMessageToEmail(String email, String message, MultipartFile file, String subject) {
        try {
            mailSender.sendFile(email, subject, message, file);
        } catch (MessagingException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}
