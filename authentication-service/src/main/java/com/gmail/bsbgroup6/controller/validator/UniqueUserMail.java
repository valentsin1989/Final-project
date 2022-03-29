package com.gmail.bsbgroup6.controller.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueUserMailValidator.class)
public @interface UniqueUserMail {
    String message() default "A user with this email address already exists. Please enter a different email";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
