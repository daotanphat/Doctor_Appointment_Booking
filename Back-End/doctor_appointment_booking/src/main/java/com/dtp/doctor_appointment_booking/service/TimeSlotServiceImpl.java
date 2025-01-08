package com.dtp.doctor_appointment_booking.service;

import com.dtp.doctor_appointment_booking.exception.EntityNotFoundException;
import com.dtp.doctor_appointment_booking.model.TimeSlot;
import com.dtp.doctor_appointment_booking.repository.TimeSlotRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class TimeSlotServiceImpl implements TimeSlotService {
    private final TimeSlotRepository timeSlotRepository;

    public TimeSlotServiceImpl(TimeSlotRepository timeSlotRepository) {
        this.timeSlotRepository = timeSlotRepository;
    }

    @Override
    public boolean isTimeSlotAvailable(int timeSlotFrom, int timeSlotTo, int doctorId, LocalDate date) {
        return false;
    }

    @Override
    public TimeSlot getTimeSlot(int timeSlotId) {
        return timeSlotRepository.findById(timeSlotId)
                .orElseThrow(() -> new EntityNotFoundException(TimeSlot.class));
    }
}
