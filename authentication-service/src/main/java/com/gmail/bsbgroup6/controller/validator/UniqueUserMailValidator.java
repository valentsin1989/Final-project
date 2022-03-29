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
public class UniqueUserMailValidator implements ConstraintValidator<UniqueUserMail, String> {

    private final UserService userService;

    @Override
    public boolean isValid(String userMail, ConstraintValidatorContext constraintValidatorContext) {
        UserDTO userDTO = userService.getByUserMail(userMail);
        return userDTO == null;
    }
}
