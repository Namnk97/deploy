package com.nextsol.khangbb.exception;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<CheckEmail, String> {
    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null) {
            return true;
        }
        if (email.matches("\\w+@\\w+[.]\\w+([.]\\w+)?")) {
            return true;
        }
        return false;
    }
}
