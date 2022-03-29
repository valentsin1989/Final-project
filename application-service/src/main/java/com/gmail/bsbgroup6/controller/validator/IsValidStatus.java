package com.gmail.bsbgroup6.controller.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IsValidStatusValidator.class)
public @interface IsValidStatus {
    String message() default "Статус не может быть изменен";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
