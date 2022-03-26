package com.gmail.bsbgroup6.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EmployeeDTO {
    @JsonProperty("Employee Id")
    private final Long id;
    @JsonProperty("Full_Name_Individual")
    private final String fullName;
    @JsonProperty("Recruitment_date")
    private final String recruitmentDate;
    @JsonProperty("Termination_date")
    private final String terminationDate;
    @JsonProperty("Name_Legal")
    private final String legalEntityName;
    @JsonProperty("Person_Iban_Byn")
    private final String personIbanByn;
    @JsonProperty("Person_Iban_Currency")
    private final String personIbanCurrency;
}
