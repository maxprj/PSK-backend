package com.psk.backend.domain.trip.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidTripDatesValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ValidTripDates {
    String message() default "{com.psk.backend.domain.user.validation" +
            ".ValidTripDatesValidator.message}";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}