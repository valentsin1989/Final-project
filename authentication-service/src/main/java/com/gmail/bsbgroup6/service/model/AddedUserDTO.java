package com.gmail.bsbgroup6.service.model;

import lombok.Data;

@Data
public class AddedUserDTO {
    private Long userId;
    private UserStatusEnum status;
}
