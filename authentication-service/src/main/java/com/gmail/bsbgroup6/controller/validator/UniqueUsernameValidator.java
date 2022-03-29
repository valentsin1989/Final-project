package com.gmail.bsbgroup6.controller.validator;

import com.gmail.bsbgroup6.service.UserService;
import com.gmail.bsbgroup6.service.model.UserDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    private final UserService userService;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        UserDTO userDTO = userService.getByUsername(username);
        return userDTO == null;
    }
}
