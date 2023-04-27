package com.example.contoso.dto.request;

import lombok.*;

/**
 * @author Neevels
 * @version 1.0
 * @date 4/27/2023 6:53 PM
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class MessageRequest {
    private String message;
}
