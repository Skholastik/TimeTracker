package com.timetracker.Service.AncillaryServices.CustomAnnotation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CustomReportTypeValidator.class)
@Target( { ElementType.FIELD,ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomReportTypeValid {
    String message() default "{CustomProjectTypeValid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

