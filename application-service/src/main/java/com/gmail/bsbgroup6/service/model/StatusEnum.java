package com.gmail.bsbgroup6.service.model;

public enum StatusEnum {
    NEW("New"),
    IN_PROGRESS("In progress"),
    DONE("Done"),
    REJECTED("Rejected");

    private final String value;

    StatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
