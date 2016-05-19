package com.timetracker.Service.AncillaryServices.CustomAnnotation;

import com.timetracker.Entities.Status;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class CustomStatusValidator implements ConstraintValidator<CustomStatusValid, String> {

    @Override
    public void initialize(CustomStatusValid statusConstraint) {

    }

    @Override
    public boolean isValid(String statusField, ConstraintValidatorContext constraintContext) {

        if (StringUtils.isBlank(statusField)) {
            constraintContext.disableDefaultConstraintViolation();
            constraintContext.buildConstraintViolationWithTemplate("Поле статуса не может быть пустым")
                    .addConstraintViolation();
            return false;
        }

        for (Status status : Status.values()) {
            if (statusField.equals(status.getStatus()))
                return true;
        }
        return false;
    }
}
