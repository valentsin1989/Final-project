package com.gmail.bsbgroup6.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApplicationDTO {
    @JsonProperty("ApplicationConvId")
    private String uniqueNumber;
    @JsonProperty("Status")
    private String status;
    @JsonProperty("EmployeeId")
    private Long employeeId;
    @JsonProperty("Full_Name_Individual")
    private String employeeFullName;
    @JsonProperty("Percent_Conv")
    private Float conversionPercentage;
    @JsonProperty("Value_Leg")
    private String valueLegal;
    @JsonProperty("Value_Ind")
    private String valueIndividual;
    @JsonProperty("Name_Legal")
    private String legalEntityName;
}
