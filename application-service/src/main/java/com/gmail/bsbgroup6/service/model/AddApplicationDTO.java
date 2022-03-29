package com.gmail.bsbgroup6.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddApplicationDTO {
    @JsonProperty("ApplicationConvId")
    private String uniqueNumber;
    @JsonProperty("Value_Leg")
    private String valueLegal;
    @JsonProperty("Value_Ind")
    private String valueIndividual;
    @JsonProperty("EmployeeId")
    private Long employeeId;
    @JsonProperty("Percent_Conv")
    private Float conversionPercentage;
    @JsonProperty("Name_Legal")
    private String legalEntityName;
}
