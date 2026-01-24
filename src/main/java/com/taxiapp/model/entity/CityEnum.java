package com.taxiapp.model.entity;

import lombok.Getter;

@Getter
public enum CityEnum {

    NIS ( "Drive from Nis starts at University of Nis start point."),
    BELGRADE ( "Drive from Belgrade starts at University of Belgrade start point."),
    KRAGUJEVAC ( "Drive from Kragujevac starts at University of Kragujevac start point.");

    private final String description;

    CityEnum(String description) {
        this.description=description;
    }

    public String getDescription() {
        return description;
    }
}
