package com.nextsol.khangbb.exception;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<CheckPhone, String> {

    @Override
    public boolean isValid(String phoneNo, ConstraintValidatorContext context) {
        if (phoneNo == null) {
            return false;
        }
        return phoneNo.matches("\\d{10}");
    }
}
