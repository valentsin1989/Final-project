package com.gmail.bsbgroup6.service.model;

import com.gmail.bsbgroup6.controller.validator.BlockedUser;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@BlockedUser
public class LoginDTO {
    @Size(min = 6, max = 100, message = "Login or password not valid.")
    @Pattern(regexp = "\\A[a-z]+\\z", message = "Login or password not valid.")
    private String username;
    @Size(max = 20, message = "Login or password not valid.")
    @Pattern(regexp = "\\A[a-z1-9]+@[a-z]+\\.[a-z]+\\z", message = "Login or password not valid.")
    private String usermail;
    @NotNull(message = "Login or password not valid.")
    @Size(min = 8, max = 20, message = "Login or password not valid.")
    private String password;
}
