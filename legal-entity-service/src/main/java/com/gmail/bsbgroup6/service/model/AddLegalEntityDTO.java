package com.gmail.bsbgroup6.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gmail.bsbgroup6.controller.validator.IfExists;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@IfExists
public class AddLegalEntityDTO {
    @NotBlank(message = "Поле Name_Legal не может быть пустым")
    @Size(max = 255, message = "Неверно заданы параметры")
    @JsonProperty("Name_Legal")
    private final String name;

    @Min(value = 100000000, message = "Неверно заданы параметры")
    @Max(value = 999999999, message = "Неверно заданы параметры")
    @JsonProperty("UNP")
    private final Integer unp;

    @NotBlank(message = "Неверно заданы параметры")
    @Size(min = 28, max = 28, message = "Неверно заданы параметры")
    @Pattern(regexp = "^BY\\d{2}[A-Z0-9]{4}\\d{4}[A-Z0-9]{16}$", message = "Неверно заданы параметры")
    @JsonProperty("IBANbyBYN")
    private final String ibanByByn;

    @JsonProperty("Type_Legal")
    private final Boolean type;

    @Max(value = 1000, message = "Неверно заданы параметры")
    @JsonProperty("Total_Employees")
    private final Integer totalEmployees;
}
