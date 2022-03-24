package com.gmail.bsbgroup6.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gmail.bsbgroup6.controller.validator.IfExists;
import lombok.Data;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@IfExists
public class AddEmployeeDTO {
    @NotBlank(message = "Поле Full_Name_Individual не может быть пустым")
    @Size(max = 255, message = "Неверно заданы параметры")
    @JsonProperty("Full_Name_Individual")
    private final String fullName;

    @PastOrPresent(message = "Неверно заданы параметры")
    @JsonProperty("Recruitment_date")
    private final LocalDate recruitmentDate;

    @FutureOrPresent(message = "Неверно заданы параметры")
    @JsonProperty("Termination_date")
    private final LocalDate terminationDate;

    @JsonProperty("Name_Legal")
    private final String legalEntityName;

    @NotBlank(message = "Неверно заданы параметры")
    @Size(min = 28, max = 28, message = "Неверно заданы параметры")
    @Pattern(regexp = "^BY\\d{2}[A-Z0-9]{4}\\d{4}[A-Z0-9]{16}$", message = "Неверно заданы параметры")
    @JsonProperty("Person_Iban_Byn")
    private final String personIbanByn;

    @NotBlank(message = "Неверно заданы параметры")
    @Size(min = 28, max = 28, message = "Неверно заданы параметры")
    @Pattern(regexp = "^BY\\d{2}[A-Z0-9]{4}\\d{4}[A-Z0-9]{16}$", message = "Неверно заданы параметры")
    @JsonProperty("Person_Iban_Currency")
    private final String personIbanCurrency;
}
