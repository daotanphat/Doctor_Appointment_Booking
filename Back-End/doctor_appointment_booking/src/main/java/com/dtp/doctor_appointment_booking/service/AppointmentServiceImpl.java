package com.dtp.doctor_appointment_booking.service;

import com.dtp.doctor_appointment_booking.dto.request.BookAppointmentRequest;
import com.dtp.doctor_appointment_booking.exception.IllegalArgumentException;
import com.dtp.doctor_appointment_booking.exception.SlotUnavailableException;
import com.dtp.doctor_appointment_booking.mapper.AppointmentMapper;
import com.dtp.doctor_appointment_booking.model.*;
import com.dtp.doctor_appointment_booking.model.compositeKey.AppointmentStatusKey;
import com.dtp.doctor_appointment_booking.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private AppointmentStatusRepository appointmentStatusRepository;

    @Autowired
    private DoctorBusyRepository doctorBusyRepository;

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @Override
    public Appointment bookAppointment(BookAppointmentRequest request) {
        // check valid slot
        if (!isTimeSlotAvailable(request.getDoctor_id(), request.getDateSlot(), request.getTimeSlotFrom(), request.getTimeSlotTo())) {
            throw new SlotUnavailableException("Slot not available");
        }

        Appointment appointment = AppointmentMapper.INSTANCE.bookRequestToEntity(request);
        appointment.setPaymentStatus("PENDING");
        appointmentRepository.save(appointment); // save appointment

        // set status of appointment
        Status status = statusRepository.findByStatus("BOOKED");
        AppointmentStatus appointmentStatus = new AppointmentStatus();
        AppointmentStatusKey appointmentStatusKey = new AppointmentStatusKey();
        appointmentStatusKey.setAppointmentId(appointment.getId());
        appointmentStatusKey.setStatusId(status.getId());
        appointmentStatus.setId(appointmentStatusKey);
        appointmentStatus.setAppointment(appointment);
        appointmentStatus.setStatus(status);
        appointmentStatusRepository.save(appointmentStatus);

        appointment.getStatuses().add(appointmentStatus);
        appointmentRepository.save(appointment); // save appointment

        return appointment;
    }

    public boolean isTimeSlotAvailable(String doctorId, LocalDate date, int timeSlotFrom, int timeSlotTo) {
        Set<DoctorBusy> doctorBusies = doctorBusyRepository.findByDoctor_Doctor_idAndAndDate(doctorId, date);

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

    public boolean isSlotOverlapping(LocalTime timeStart, LocalTime timeEnd, LocalTime busyStart, LocalTime busyEnd) {
        return timeStart.isBefore(busyEnd) && timeEnd.isAfter(busyStart);
    }
}
