package com.example.contoso.service;

import com.example.contoso.dto.request.RequestRequest;
import com.example.contoso.dto.response.RequestResponse;
import com.example.contoso.entity.Request;

import java.util.List;

/**
 * @author Neevels
 * @version 1.0
 * @date 4/27/2023 10:16 PM
 */
public interface RequestService {
    void addRequest(RequestRequest requestRequest);
    void updateRequest(RequestRequest requestRequest, Integer id);
    void deleteRequest(Integer id);
    void changeStatusToCancelled(Integer requestId);
    List<RequestResponse> getById(Integer userId);
    void changeStatus(Integer requestId, Request.StatusOfRequest status);
}
