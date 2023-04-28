package com.example.contoso.controller;

import com.example.contoso.dto.request.ClientRequest;
import com.example.contoso.dto.request.MessageRequest;
import com.example.contoso.dto.response.ClientResponse;
import com.example.contoso.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author Neevels
 * @version 1.0
 * @date 4/27/2023 6:11 PM
 */
@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<String> addClient(@RequestBody ClientRequest clientRequest) {
        clientService.addClient(clientRequest);
        return ResponseEntity.ok("Клиент успешно добавлен!");
    }

    @GetMapping
    public ResponseEntity<List<ClientResponse>> getAll() {
        return ResponseEntity
                .ok()
                .body(clientService.getAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteClient(@PathVariable Integer id) {
        clientService.deleteClient(id);
        return ResponseEntity.ok("Клиент успешно удален!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProduct(@RequestBody ClientRequest clientRequest,
                                                @PathVariable Integer id) {
        clientService.updateClient(clientRequest, id);
        return ResponseEntity.ok("Клиент успешно обновлен!");
    }

    @PostMapping("message/{id}")
    public ResponseEntity<String> sendMessageToClient(@RequestParam("file") MultipartFile file,
                                                      @RequestPart("messageRequest") MessageRequest messageRequest,
                                                      @PathVariable Integer id) {
        clientService.sendMessageById(messageRequest, id, file);
        return ResponseEntity.ok("Сообщение успешно отправлено!");
    }

    @PostMapping("message")
    public ResponseEntity<String> sendMessageToAll(@RequestParam("file") MultipartFile file,
                                                   @RequestPart("messageRequest") MessageRequest messageRequest) {
        clientService.sendMessageToAll(messageRequest, file);
        return ResponseEntity.ok("Сообщение успешно отправлено!");
    }
}
