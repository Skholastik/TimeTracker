package com.timetracker.Service.AncillaryServices.CustomAnnotation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CustomIDValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomIDValid {
    String message() default "{CustomIDValid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

