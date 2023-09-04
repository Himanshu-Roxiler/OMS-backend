package com.roxiler.erp.aspects;

import com.roxiler.erp.interfaces.ValidateString;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.ArrayList;
import java.util.List;

public class StringValidator implements ConstraintValidator<ValidateString, String> {

    private List<String> valueList;

    @Override
    public void initialize(ValidateString constraintAnnotation) {
        valueList = new ArrayList<>();
        for (String val : constraintAnnotation.acceptedValues()) {
            valueList.add(val.toUpperCase());
        }
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return valueList.contains(value.toUpperCase());
    }

}
