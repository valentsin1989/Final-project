package com.gmail.bsbgroup6.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AddedEmployeeDTO {
    @JsonProperty("Employee Id")
    private Long id;
}
