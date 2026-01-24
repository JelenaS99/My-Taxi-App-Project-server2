package com.taxiapp.model.entity;

import lombok.Getter;

@Getter
public enum CarEnum {

    REGULAR ("This car type can drive from 1-4 person."),
    CARGO ("This car type can drive up to 2 person, and cargo up to 500kg."),
    VAN ("This car type can drive from 1-8 person, recommended for bigger groups.");


    private final String description;

    CarEnum(String description) {
        this.description=description;
    }

    public String getDescription() {
        return description;
    }
}
