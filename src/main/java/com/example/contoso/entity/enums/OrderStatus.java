package com.example.contoso.entity.enums;

/**
 * @author Neevels
 * @version 1.0
 * @date 5/5/2023 2:36 PM
 */
public enum OrderStatus {
    CANCELLED("Отклонен"),
    DECORATED("Оформлен"),
    COMPLETED("Выполнен");

    private final String url;

    OrderStatus(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
