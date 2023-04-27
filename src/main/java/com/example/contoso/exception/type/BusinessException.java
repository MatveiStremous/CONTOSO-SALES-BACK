package com.example.contoso.exception.type;

import org.springframework.http.HttpStatus;

/**
 * @author Neevels
 * @version 1.0
 * @date 4/5/2023 3:08 PM
 */
public class BusinessException extends RuntimeException {
    private HttpStatus httpStatus;

    public BusinessException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
