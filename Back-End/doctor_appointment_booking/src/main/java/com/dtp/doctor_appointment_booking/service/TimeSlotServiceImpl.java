package com.dtp.doctor_appointment_booking.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class TimeSlotServiceImpl implements TimeSlotService{
    @Override
    public boolean isTimeSlotAvailable(int timeSlotFrom, int timeSlotTo, int doctorId, LocalDate date) {
        return false;
    }
}
