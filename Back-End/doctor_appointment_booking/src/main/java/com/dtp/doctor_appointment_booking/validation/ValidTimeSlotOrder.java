package com.dtp.doctor_appointment_booking.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TimeSlotOrderValidator.class)
public @interface ValidTimeSlotOrder {
    String message() default "timeSlotTo must be later than timeSlotFrom";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
