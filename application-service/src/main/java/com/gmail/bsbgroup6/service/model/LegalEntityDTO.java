package com.gmail.bsbgroup6.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LegalEntityDTO {
    @JsonProperty("LegalId")
    private Long id;
    @JsonProperty("Name_Legal")
    private String name;
    @JsonProperty("UNP")
    private Integer unp;
    @JsonProperty("IBANbyBYN")
    private String ibanByByn;
    @JsonProperty("Type_Legal")
    private String type;
    @JsonProperty("Total_Employees")
    private Integer totalEmployees;
}
