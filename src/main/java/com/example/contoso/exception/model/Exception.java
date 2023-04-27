package com.example.contoso.exception.model;

import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

/**
 * @author Neevels
 * @version 1.0
 * @date 3/15/2023 12:15 PM
 */
@Builder
public record Exception(String message, HttpStatus httpStatus, ZonedDateTime timestamp) {
}
