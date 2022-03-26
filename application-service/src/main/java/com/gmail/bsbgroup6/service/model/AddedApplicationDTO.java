package com.gmail.bsbgroup6.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AddedApplicationDTO {
    @JsonProperty("ApplicationConvId")
    private String uniqueNumber;
}
