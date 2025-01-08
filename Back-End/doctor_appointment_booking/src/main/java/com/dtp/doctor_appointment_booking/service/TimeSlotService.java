package com.dtp.doctor_appointment_booking.service;

import com.dtp.doctor_appointment_booking.model.TimeSlot;

import java.time.LocalDate;
import java.time.LocalTime;

public interface TimeSlotService {
    boolean isTimeSlotAvailable(int timeSlotFrom, int timeSlotTo, int doctorId, LocalDate date);

    TimeSlot getTimeSlot(int timeSlotId);
}
