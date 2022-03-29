package com.gmail.bsbgroup6.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UpdatedByStatusApplicationDTO {
    @JsonProperty("status")
    private String status;
    @JsonProperty("User")
    private String user;
}
