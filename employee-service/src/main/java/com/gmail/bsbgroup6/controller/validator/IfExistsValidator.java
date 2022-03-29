package com.gmail.bsbgroup6.controller.validator;

import com.gmail.bsbgroup6.service.model.AddEmployeeDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IfExistsValidator implements ConstraintValidator<IfExists, AddEmployeeDTO> {

    private final EmployeeValidator employeeValidator;

    @Override
    public boolean isValid(AddEmployeeDTO addEmployeeDTO, ConstraintValidatorContext constraintValidatorContext) {
        return !employeeValidator.isEmployeeExists(addEmployeeDTO);
    }
}
