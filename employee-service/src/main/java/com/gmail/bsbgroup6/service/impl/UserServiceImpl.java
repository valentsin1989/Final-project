package com.gmail.bsbgroup6.service.impl;

import com.gmail.bsbgroup6.repository.UserRepository;
import com.gmail.bsbgroup6.repository.model.StatusEnum;
import com.gmail.bsbgroup6.repository.model.User;
import com.gmail.bsbgroup6.service.UserService;
import com.gmail.bsbgroup6.service.model.*;
import lombok.AllArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    @Transactional
    public AddedUserDTO addUser(AddUserDTO addUserDTO) {
        User user = convertToUser(addUserDTO);
        userRepository.add(user);
        Long id = user.getId();
        if (id == null) {
            throw new ServiceException("User not created.");
        }
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("hh.mm dd.MM.yyyy");
        String dateString = dateTimeFormatter.format(localDateTime);
        user.setCreatedDate(dateString);
        return convertToAddedUserDTO(user);
    }

    @Override
    @Transactional
    public UserDTO getByUsername(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return null;
        }
        return convertToUserDTO(user);

    }

    @Override
    @Transactional
    public UserDTO getByUserMail(String userMail) {
        User user = userRepository.findByUserMail(userMail).orElse(null);
        if (user == null) {
            return null;
        }
        return convertToUserDTO(user);

    }

    @Override
    @Transactional
    public boolean addLoginFailedByUsername(String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user != null) {
            Integer loginFailed = user.getLoginFailed();
            user.setLoginFailed(++loginFailed);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean addLoginFailedByUserMail(String userMail) {
        User user = userRepository.findByUserMail(userMail).orElse(null);
        if (user != null) {
            Integer loginFailed = user.getLoginFailed();
            user.setLoginFailed(++loginFailed);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean isBlockedUser(LoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        String mail = loginDTO.getUserMail();
        if (username != null) {
            User user = userRepository.findByUsername(username).orElse(null);
            if (user != null) {
                Integer loginFailed = user.getLoginFailed();
                if (loginFailed >= 5) {
                    user.setStatus(StatusEnum.DISABLE);
                    return true;
                }
            }
        } else if (mail != null) {
            User user = userRepository.findByUserMail(mail).orElse(null);
            if (user != null) {
                Integer loginFailed = user.getLoginFailed();
                if (loginFailed >= 5) {
                    user.setStatus(StatusEnum.DISABLE);
                    return true;
                }
            }
        }
        return false;
    }

    private UserDTO convertToUserDTO(User user) {
        Long id = user.getId();
        String username = user.getUsername();
        String password = user.getPassword();
        UserDTO userDTO = new UserDTO();
        userDTO.setId(id);
        userDTO.setUsername(username);
        userDTO.setPassword(password);
        return userDTO;
    }

    private User convertToUser(AddUserDTO addUserDTO) {
        String username = addUserDTO.getUsername();
        String password = addUserDTO.getPassword();
        String encryptedPassword = passwordEncoder.encode(password);
        StatusEnum status = StatusEnum.valueOf(UserStatusEnum.ACTIVE.name());
        String mail = addUserDTO.getUsermail();
        String firstName = addUserDTO.getFirstName();
        User user = new User();
        user.setUsername(username);
        user.setPassword(encryptedPassword);
        user.setStatus(status);
        user.setMail(mail);
        user.setFirstName(firstName);
        user.setLoginFailed(0);
        return user;
    }

    private AddedUserDTO convertToAddedUserDTO(User user) {
        Long id = user.getId();
        StatusEnum statusEnum = user.getStatus();
        UserStatusEnum status = UserStatusEnum.valueOf(statusEnum.toString());
        AddedUserDTO addedUserDTO = new AddedUserDTO();
        addedUserDTO.setUserId(id);
        addedUserDTO.setStatus(status);
        return addedUserDTO;
    }
}
