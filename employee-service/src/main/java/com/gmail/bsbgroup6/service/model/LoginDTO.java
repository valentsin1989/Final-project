package com.gmail.bsbgroup6.service.model;

import com.gmail.bsbgroup6.controller.validator.BlockedUser;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
@BlockedUser
public class LoginDTO {
    private String username;
    @Size(max = 20, message = "Usermail size cannot be 20.")
    private String userMail;
    private String password;
}
