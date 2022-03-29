package com.gmail.bsbgroup6.controller.validator;

import com.gmail.bsbgroup6.service.model.StatusUpdateApplicationDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IsValidStatusValidator implements ConstraintValidator<IsValidStatus, StatusUpdateApplicationDTO> {

    private final StatusUpdateApplicationDTOValidator statusUpdateApplicationDTOValidator;

    @Override
    public boolean isValid(StatusUpdateApplicationDTO applicationDTO, ConstraintValidatorContext constraintValidatorContext) {
        return statusUpdateApplicationDTOValidator.isValidStatus(applicationDTO);
    }
}
