package com.example.contoso.exception.handler;

import com.example.contoso.exception.model.Exception;
import com.example.contoso.exception.type.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author Neevels
 * @version 1.0
 * @date 3/15/2023 11:35 AM
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = BusinessException.class)
    protected ResponseEntity<Object> handleBusinessException(BusinessException e) {
        HttpStatus status = e.getHttpStatus();
        Exception ex = Exception.builder()
                .message(e.getMessage())
                .timestamp(ZonedDateTime.now(ZoneId.of("Z")))
                .httpStatus(e.getHttpStatus())
                .build();
        return new ResponseEntity<>(ex, status);
    }


}
