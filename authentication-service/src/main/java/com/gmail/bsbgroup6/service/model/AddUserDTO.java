package com.gmail.bsbgroup6.service.model;

import com.gmail.bsbgroup6.controller.validator.UniqueUserMail;
import com.gmail.bsbgroup6.controller.validator.UniqueUsername;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class AddUserDTO {
    @NotNull(message = "Login cannot be empty.")
    @UniqueUsername
    @Size(min = 6, max = 100, message = "Login size must be in range 6 to 20.")
    @Pattern(regexp = "\\A[a-z]+\\z", message = "The login must consist only of Latin letters in its own case.")
    private String username;
    @NotNull(message = "Password cannot be empty.")
    @Size(min = 8, max = 20, message = "Password size must be in range 8 to 20.")
    private String password;
    @NotNull(message = "Usermail cannot be empty.")
    @UniqueUserMail
    @Size(max = 20, message = "Usermail size cannot be 20.")
    @Pattern(regexp = "\\A[a-z1-9]+@[a-z]+\\.[a-z]+\\z", message = "The mail must be format allen@example.com.")
    private String usermail;
    @NotNull(message = "Username cannot be empty.")
    @Size(max = 20, message = "Username size cannot be 20.")
    @Pattern(regexp = "\\A[а-яА-Я]+\\z", message = "The username must consist only of russian letters.")
    private String firstName;
}
