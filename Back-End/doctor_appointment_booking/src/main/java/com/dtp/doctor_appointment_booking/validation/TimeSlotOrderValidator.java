package com.dtp.doctor_appointment_booking.validation;

import com.dtp.doctor_appointment_booking.dto.request.BookAppointmentRequest;
import com.dtp.doctor_appointment_booking.model.TimeSlot;
import com.dtp.doctor_appointment_booking.repository.TimeSlotRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class TimeSlotOrderValidator implements ConstraintValidator<ValidTimeSlotOrder, BookAppointmentRequest> {
    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @Override
    public boolean isValid(BookAppointmentRequest value, ConstraintValidatorContext context) {
        if (value.getTimeSlotFrom() <= 0 || value.getTimeSlotTo() <= 0) {
            return true; // Skip validation if time slots are not set
        }

        Optional<TimeSlot> fromSlot = timeSlotRepository.findById(value.getTimeSlotFrom());
        Optional<TimeSlot> toSlot = timeSlotRepository.findById(value.getTimeSlotTo());

        // Check if both time slots exist and timeSlotTo is later than timeSlotFrom
        return fromSlot.isPresent() && toSlot.isPresent()
                && fromSlot.get().getTime().isBefore(toSlot.get().getTime());
    }
}
