package com.gmail.bsbgroup6.service.model;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LogoutDTO {
    @NotNull(message = "Username cannot be empty.")
    private String username;
    @NotNull(message = "Session cannot be empty.")
    private String sessionId;
}
