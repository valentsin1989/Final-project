package com.gmail.bsbgroup6.controller.validator;

import com.gmail.bsbgroup6.service.model.AddLegalEntityDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IfExistsValidator implements ConstraintValidator<IfExists, AddLegalEntityDTO> {

    private final LegalEntityValidator legalEntityValidator;

    @Override
    public boolean isValid(AddLegalEntityDTO legalEntityDTO, ConstraintValidatorContext constraintValidatorContext) {
        return !legalEntityValidator.isLegalEntityExists(legalEntityDTO);
    }
}
