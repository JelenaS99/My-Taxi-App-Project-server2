package com.taxiapp.model.entity;

import lombok.Getter;

@Getter
public enum PaymentMethodEnum {

    CASH ("Most common payment method."),
    CARD ("Our vehicles supports card paying.");

    private final String description;

    PaymentMethodEnum(String description) {
        this.description=description;
    }

    public String getDescription() {
        return description;
    }
}
