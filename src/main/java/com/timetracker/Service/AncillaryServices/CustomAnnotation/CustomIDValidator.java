package com.timetracker.Service.AncillaryServices.CustomAnnotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CustomIDValidator implements ConstraintValidator<CustomIDValid, Integer> {

    @Override
    public void initialize(CustomIDValid statusConstraint) { }

    @Override
    public boolean isValid(Integer IDField, ConstraintValidatorContext constraintContext) {

        if (IDField == null) {
            constraintContext.disableDefaultConstraintViolation();
            constraintContext.buildConstraintViolationWithTemplate("Поле ID не может быть пустым")
                    .addConstraintViolation();

            return false;
        }

        if (IDField < 1) {
            constraintContext.disableDefaultConstraintViolation();
            constraintContext.buildConstraintViolationWithTemplate("ID должен быть больше 0")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}