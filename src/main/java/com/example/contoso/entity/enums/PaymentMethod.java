package com.example.contoso.entity.enums;

/**
 * @author Neevels
 * @version 1.0
 * @date 4/28/2023 11:33 PM
 */
public enum PaymentMethod {
    CASH("Наличные"),
    CARD("Карта");

    private final String url;

    PaymentMethod(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
