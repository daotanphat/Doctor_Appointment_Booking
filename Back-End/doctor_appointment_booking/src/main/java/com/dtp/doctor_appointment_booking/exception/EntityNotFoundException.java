package com.dtp.doctor_appointment_booking.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(Class<?> entity, int id) {
        super("The " + entity.getSimpleName().toLowerCase() + " with id " + id + " was not found.");
    }
}
