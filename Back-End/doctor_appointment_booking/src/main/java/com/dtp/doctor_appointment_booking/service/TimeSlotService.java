package com.dtp.doctor_appointment_booking.service;

import java.time.LocalDate;

public interface TimeSlotService {
    boolean isTimeSlotAvailable(int timeSlotFrom, int timeSlotTo, int doctorId, LocalDate date);
}
