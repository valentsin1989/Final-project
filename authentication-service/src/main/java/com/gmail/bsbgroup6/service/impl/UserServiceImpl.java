package com.gmail.bsbgroup6.service.impl;

import com.gmail.bsbgroup6.repository.UserRepository;
import com.gmail.bsbgroup6.repository.model.Session;
import com.gmail.bsbgroup6.repository.model.StatusEnum;
import com.gmail.bsbgroup6.repository.model.User;
import com.gmail.bsbgroup6.service.UserService;
import com.gmail.bsbgroup6.service.model.*;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    public static final int MAX_LOGIN_ATTEMPTS = 5;
    public static final String DATE_PATTERN = "hh.mm dd.MM.yyyy";
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    @Transactional
    public AddedUserDTO addUser(AddUserDTO addUserDTO) {
        User user = convertToUser(addUserDTO);
        userRepository.add(user);
        Long id = user.getId();
        if (id == null) {
            return null;
        }
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        String dateString = dateTimeFormatter.format(localDateTime);
        user.setCreatedDate(dateString);
        return convertToAddedUserDTO(user);
    }

    @Override
    @Transactional
    public UserDTO getUser(LoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        String userMail = loginDTO.getUsermail();
        User user = userRepository.findUserByNameOrMail(username, userMail).orElse(null);
        if (user == null) {
            return null;
        }
        return convertToUserDTO(user);
    }

    @Override
    @Transactional
    public boolean addLoginFailed(LoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        String userMail = loginDTO.getUsermail();
        User user = userRepository.findUserByNameOrMail(username, userMail).orElse(null);
        if (user != null) {
            Integer loginFailed = user.getLoginFailed();
            if (loginFailed >= MAX_LOGIN_ATTEMPTS) {
                user.setStatus(StatusEnum.DISABLE);
                String dateString = getDateNowInStringFormat();
                user.setLogoutDate(dateString);
                Set<Session> sessions = user.getListSession();
                sessions.stream()
                        .filter(session -> session.getClosedDate() == null)
                        .forEach(session -> session.setClosedDate(dateString));
            } else {
                user.setLoginFailed(++loginFailed);
            }
            return true;
        }
        return false;
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
    public boolean isBlockedUser(LoginDTO loginDTO) {
        String username = loginDTO.getUsername();
        String userMail = loginDTO.getUsermail();
        User user = userRepository.findUserByNameOrMail(username, userMail).orElse(null);
        if (user != null) {
            StatusEnum status = user.getStatus();
            return status.equals(StatusEnum.DISABLE);
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

    private String getDateNowInStringFormat() {
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        return dateTimeFormatter.format(localDateTime);
    }
}
