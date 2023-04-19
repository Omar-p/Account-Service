package com.example.accountservice.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AcmeEmailValidator implements
        ConstraintValidator<AcmeEmail, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value != null) {
            return value.endsWith("@acme.com");
        }
        return false;
    }
}
