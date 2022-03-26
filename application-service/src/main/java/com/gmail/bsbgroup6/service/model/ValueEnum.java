package com.gmail.bsbgroup6.service.model;

public enum ValueEnum {
    BYN(933),
    USD(840),
    EUR(978),
    RUB(643);

    private final Integer value;

    ValueEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
