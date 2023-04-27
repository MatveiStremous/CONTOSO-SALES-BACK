package com.example.contoso.dto.request;

import lombok.*;

/**
 * @author Neevels
 * @version 1.0
 * @date 4/27/2023 11:26 AM
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ChangePasswordRequest {
    Integer userId;
    String oldPassword;
    String newPassword;
}
