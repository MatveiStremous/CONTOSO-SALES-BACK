package com.example.contoso.entity.enums;

/**
 * @author Neevels
 * @version 1.0
 * @date 4/28/2023 11:30 PM
 */
public enum StatusOfRequest {
    CANCELLED("Отклонена"),
    DECORATED("Оформлена"),
    COMPLETED("Подтверждена");

    private final String url;

    StatusOfRequest(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}