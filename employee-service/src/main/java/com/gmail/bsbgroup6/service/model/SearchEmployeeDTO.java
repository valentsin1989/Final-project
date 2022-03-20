package com.gmail.bsbgroup6.service.model;

import lombok.Data;

@Data
public class SearchEmployeeDTO {
    private String legalEntityName;
    private Integer unp;
    private String fullName;
}
