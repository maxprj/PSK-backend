package com.psk.backend.domain.calendar.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidEventFormValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ValidEventForm {
    String message() default "{com.psk.backend.domain.user.validation" +
            ".ValidEventForm.message}";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}