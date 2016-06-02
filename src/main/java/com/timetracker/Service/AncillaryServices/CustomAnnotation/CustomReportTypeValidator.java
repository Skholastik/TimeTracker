package com.timetracker.Service.AncillaryServices.CustomAnnotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class CustomReportTypeValidator implements ConstraintValidator<CustomReportTypeValid, Integer> {

    @Override
    public void initialize(CustomReportTypeValid statusConstraint) {

    }

    @Override
    public boolean isValid(Integer reportTypeField, ConstraintValidatorContext constraintContext) {

        if (reportTypeField==null) {
            constraintContext.disableDefaultConstraintViolation();
            constraintContext.buildConstraintViolationWithTemplate("Вы должны заполнить тип отчета")
                    .addConstraintViolation();
            return false;
        }
        if (reportTypeField!=1 && reportTypeField!=2){
            constraintContext.disableDefaultConstraintViolation();
            constraintContext.buildConstraintViolationWithTemplate("Недопустимый тип отчета")
                    .addConstraintViolation();
            return false;
        }


        return true;
    }
}