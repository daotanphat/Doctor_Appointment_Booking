package com.dtp.doctor_appointment_booking.validation;

import com.dtp.doctor_appointment_booking.dto.appointment.BookAppointmentRequest;
import com.dtp.doctor_appointment_booking.model.TimeSlot;
import com.dtp.doctor_appointment_booking.repository.TimeSlotRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class TimeSlotOrderValidator implements ConstraintValidator<ValidTimeSlotOrder, TimeSlotOrder> {
    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @Override
    public boolean isValid(TimeSlotOrder value, ConstraintValidatorContext context) {
        if (value.timeFrom() <= 0 || value.timeTo() <= 0) {
            return true; // Skip validation if time slots are not set
        }

        Optional<TimeSlot> fromSlot = timeSlotRepository.findById(value.timeFrom());
        Optional<TimeSlot> toSlot = timeSlotRepository.findById(value.timeTo());

        // Check if both time slots exist and timeSlotTo is later than timeSlotFrom
        return fromSlot.isPresent() && toSlot.isPresent()
                && fromSlot.get().getTime().isBefore(toSlot.get().getTime());
    }
}
