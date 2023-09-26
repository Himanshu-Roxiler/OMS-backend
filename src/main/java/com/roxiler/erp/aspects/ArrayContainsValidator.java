package com.roxiler.erp.aspects;

import com.roxiler.erp.interfaces.Contains;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ArrayContainsValidator implements ConstraintValidator<Contains, String[]> {

    private String[] allowedValues;

    @Override
    public void initialize(Contains constraint) {
        allowedValues = constraint.values();
    }

    @Override
    public boolean isValid(String[] values, ConstraintValidatorContext context) {
        if (values == null) {
            return true; // Null array is considered valid
        }

        for (String value : values) {
            boolean isValid = false;
            for (String allowedValue : allowedValues) {
                if (value.equals(allowedValue)) {
                    isValid = true;
                    break;
                }
            }

            if (!isValid) {
                return false; // Element is not in the list of allowed values
            }
        }

        return true; // All elements are valid
    }
}
