package com.gmail.bsbgroup6.service;

import com.gmail.bsbgroup6.service.model.AddUserDTO;
import com.gmail.bsbgroup6.service.model.AddedUserDTO;
import com.gmail.bsbgroup6.service.model.LoginDTO;
import com.gmail.bsbgroup6.service.model.UserDTO;

public interface UserService {
    UserDTO getUser(LoginDTO loginDTO);

    boolean addLoginFailed(LoginDTO loginDTO);

    UserDTO getByUsername(String username);

    UserDTO getByUserMail(String userMail);

    AddedUserDTO addUser(AddUserDTO addUserDTO);

    boolean isBlockedUser(LoginDTO loginDTO);
}
