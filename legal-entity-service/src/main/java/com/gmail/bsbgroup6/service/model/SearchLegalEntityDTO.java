package com.gmail.bsbgroup6.service.model;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Data
public class SearchLegalEntityDTO {
    @Size(min = 3, message = "Неверно заданы параметры")
    private String name;
    @Min(value = 3, message = "Неверно заданы параметры")
    private Integer unp;
    @Size(min = 3, message = "Неверно заданы параметры")
    private String ibanByByn;
}
