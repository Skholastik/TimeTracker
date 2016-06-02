package com.timetracker.Service.AncillaryServices.CustomAnnotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CustomStatusValidator.class)
@Target( { ElementType.FIELD,ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomStatusValid {
    String message() default "{CustomStatusValid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

