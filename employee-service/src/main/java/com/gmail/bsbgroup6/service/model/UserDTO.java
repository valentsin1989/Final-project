package com.gmail.bsbgroup6.service.model;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String password;
}
