package com.gmail.bsbgroup6.controller.validator;

import com.gmail.bsbgroup6.service.UserService;
import com.gmail.bsbgroup6.service.model.LoginDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
@AllArgsConstructor
public class BlockedUserValidator implements ConstraintValidator<BlockedUser, LoginDTO> {

    private final UserService userService;

    @Override
    public boolean isValid(LoginDTO loginDTO, ConstraintValidatorContext constraintValidatorContext) {

        return !userService.isBlockedUser(loginDTO);
    }
}
