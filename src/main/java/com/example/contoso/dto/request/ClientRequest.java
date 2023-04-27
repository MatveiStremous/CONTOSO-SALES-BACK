package com.example.contoso.dto.request;

import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.LocalDateTime;

/**
 * @author Neevels
 * @version 1.0
 * @date 4/27/2023 6:11 PM
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ClientRequest {
    private String name;
    @Email(message = "invalid email")
    private String email;
    private String address;
    private String phoneNumber;
}
