package com.dtp.doctor_appointment_booking.utils;

import com.dtp.doctor_appointment_booking.model.DoctorBusy;
import com.dtp.doctor_appointment_booking.model.TimeSlot;
import com.dtp.doctor_appointment_booking.repository.DoctorBusyRepository;
import com.dtp.doctor_appointment_booking.repository.TimeSlotRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class TimeSlotUtils {
    public static boolean isTimeSlotAvailable(DoctorBusyRepository doctorBusyRepository,
                                              TimeSlotRepository timeSlotRepository, String doctorId, LocalDate date,
                                              int timeSlotFrom, int timeSlotTo) {
        List<DoctorBusy> doctorBusies = doctorBusyRepository.findByDoctorIdAndDate(doctorId, date);

        TimeSlot requestedTimeSlotStart = timeSlotRepository.findById(timeSlotFrom).orElseThrow();
        TimeSlot requestedTimeSlotEnd = timeSlotRepository.findById(timeSlotTo).orElseThrow();

        LocalTime timeStart = requestedTimeSlotStart.getTime();
        LocalTime timeEnd = requestedTimeSlotEnd.getTime();

        for (DoctorBusy doctorBusy : doctorBusies) {
            LocalTime doctorBusyTimeStart = doctorBusy.getTimeSlotFrom().getTime();
            LocalTime doctorBusyTimeEnd = doctorBusy.getTimeSlotTo().getTime();

            if (isSlotOverlapping(timeStart, timeEnd, doctorBusyTimeStart, doctorBusyTimeEnd)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isSlotOverlapping(LocalTime timeStart, LocalTime timeEnd, LocalTime busyStart, LocalTime busyEnd) {
        return timeStart.isBefore(busyEnd) && timeEnd.isAfter(busyStart);
    }
}
