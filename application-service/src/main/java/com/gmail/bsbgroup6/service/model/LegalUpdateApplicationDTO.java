package com.gmail.bsbgroup6.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LegalUpdateApplicationDTO {
    @JsonProperty("ApplicationConvId")
    private String applicationConvId;
    @JsonProperty("Name_Legal")
    private String legalEntityName;
}
