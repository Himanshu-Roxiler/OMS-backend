package com.roxiler.erp.interfaces;

import jakarta.validation.Payload;

public @interface ValidateString {

    String[] acceptedValues();

    String message() default "{uk.dds.ideskos.validator.ValidateString.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
