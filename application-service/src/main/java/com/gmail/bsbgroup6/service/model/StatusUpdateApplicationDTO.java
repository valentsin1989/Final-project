package com.gmail.bsbgroup6.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gmail.bsbgroup6.controller.validator.IsValidStatus;
import lombok.Data;

@Data
@IsValidStatus
public class StatusUpdateApplicationDTO {
    @JsonProperty("applicationConvId")
    private String applicationConvId;
    @JsonProperty("status")
    private String status;
}
