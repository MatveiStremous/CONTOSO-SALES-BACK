package com.example.contoso.dto.request.user;

import jakarta.validation.constraints.Email;
import lombok.*;

/**
 * @author Neevels
 * @version 1.0
 * @date 4/27/2023 4:40 PM
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class LoginRequest {
    @Email(message = "invalid email")
    private String login;
    private String password;
}
