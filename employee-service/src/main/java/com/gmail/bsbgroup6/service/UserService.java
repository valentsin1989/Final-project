package com.gmail.bsbgroup6.service;

import com.gmail.bsbgroup6.service.model.AddUserDTO;
import com.gmail.bsbgroup6.service.model.AddedUserDTO;
import com.gmail.bsbgroup6.service.model.LoginDTO;
import com.gmail.bsbgroup6.service.model.UserDTO;

public interface UserService {
    UserDTO getByUsername(String username);

    UserDTO getByUserMail(String usermail);

    AddedUserDTO addUser(AddUserDTO addUserDTO);

    boolean addLoginFailedByUsername(String username);

    boolean addLoginFailedByUserMail(String usermail);

    boolean isBlockedUser(LoginDTO loginDTO);
}
