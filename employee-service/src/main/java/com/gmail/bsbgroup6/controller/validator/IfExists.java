package com.gmail.bsbgroup6.controller.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IfExistsValidator.class)
public @interface IfExists {
    String message() default "Сотрудник существует";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}