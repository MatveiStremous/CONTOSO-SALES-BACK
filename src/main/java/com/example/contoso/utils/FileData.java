package com.example.contoso.utils;

import lombok.*;

/**
 * @author Neevels
 * @version 1.0
 * @date 5/11/2023 7:41 PM
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class FileData {
    private String code;
    private Integer amount;
}
