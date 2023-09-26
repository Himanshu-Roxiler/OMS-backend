package com.roxiler.erp.interfaces;

import com.roxiler.erp.aspects.ArrayContainsValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ArrayContainsValidator.class)
public @interface Contains {
    String[] values();

    String message() default "Invalid value in array";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
