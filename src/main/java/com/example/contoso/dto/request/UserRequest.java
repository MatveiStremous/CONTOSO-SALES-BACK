package com.example.contoso.dto.request;

import lombok.*;

/**
 * @author Neevels
 * @version 1.0
 * @date 4/27/2023 11:24 AM
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserRequest {
    private String firstName;
    private String lastName;
    private String login;
    private String phoneNumber;
    private String password;
}
