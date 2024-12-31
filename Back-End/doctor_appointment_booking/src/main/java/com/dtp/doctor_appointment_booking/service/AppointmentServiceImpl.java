package com.dtp.doctor_appointment_booking.service;

import com.dtp.doctor_appointment_booking.dto.appointment.BookAppointmentRequest;
import com.dtp.doctor_appointment_booking.exception.EntityNotFoundException;
import com.dtp.doctor_appointment_booking.exception.SlotUnavailableException;
import com.dtp.doctor_appointment_booking.mapper.AppointmentMapper;
import com.dtp.doctor_appointment_booking.model.*;
import com.dtp.doctor_appointment_booking.model.compositeKey.AppointmentStatusKey;
import com.dtp.doctor_appointment_booking.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final StatusRepository statusRepository;
    private final AppointmentStatusRepository appointmentStatusRepository;
    private final DoctorBusyRepository doctorBusyRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final AppointmentMapper appointmentMapper;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, StatusRepository statusRepository,
                                  AppointmentStatusRepository appointmentStatusRepository, DoctorBusyRepository doctorBusyRepository,
                                  TimeSlotRepository timeSlotRepository, DoctorRepository doctorRepository,
                                  UserRepository userRepository, AppointmentMapper appointmentMapper) {
        this.appointmentRepository = appointmentRepository;
        this.statusRepository = statusRepository;
        this.appointmentStatusRepository = appointmentStatusRepository;
        this.doctorBusyRepository = doctorBusyRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
        this.appointmentMapper = appointmentMapper;
    }

    @Transactional
    @Override
    public Appointment bookAppointment(BookAppointmentRequest request) {
        // check valid slot
        if (!isTimeSlotAvailable(request.getDoctor_id(), request.getDateSlot(), request.getTimeSlotFrom(), request.getTimeSlotTo())) {
            throw new SlotUnavailableException("Slot not available");
        }

        Doctor doctor = doctorRepository.findByDoctor_id(request.getDoctor_id())
                .orElseThrow(() -> new EntityNotFoundException(Doctor.class));

        User user = userRepository.findByEmail(request.getPatient_mail())
                .orElseThrow(() -> new EntityNotFoundException(User.class));

        Appointment appointment = appointmentMapper.bookRequestToEntity(request);
        appointment.setDoctor(doctor);
        appointment.setPatient(user);
        appointment.setPaymentStatus("PENDING");
        appointmentRepository.save(appointment); // save appointment

        // add status to appointment status
        AppointmentStatus appointmentStatus = addAppointmentStatus(appointment.getAppointment_id(), "BOOKED");

        return appointment;
    }

    @Override
    public Page<Appointment> getMyAppointments(String email, String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Appointment> appointments = appointmentRepository.findAllByPatientEmail(email, search, pageable);
        return appointments;
    }

    @Override
    @Transactional
    public Appointment cancelAppointment(String appointmentId) {
        AppointmentStatus appointmentStatus = addAppointmentStatus(appointmentId, "CANCEL");

        // change status of payment to cancelled
        Appointment appointment = appointmentStatus.getAppointment();
        appointment.setPaymentStatus("CANCELLED");
        appointmentRepository.save(appointment);

        return appointment;
    }

    @Override
    public Appointment getAppointment(String appointmentId) {
        Appointment appointment = appointmentRepository.findByAppointment_id(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException(Appointment.class));
        return appointment;
    }

    public AppointmentStatus addAppointmentStatus(String appointmentId, String status) {
        Appointment appointment = appointmentRepository.findByAppointment_id(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException(Appointment.class));

        Status statusObject = statusRepository.findByStatus(status);

        AppointmentStatus appointmentStatus = new AppointmentStatus();
        AppointmentStatusKey appointmentStatusKey = new AppointmentStatusKey();
        appointmentStatusKey.setAppointmentId(appointment.getId());
        appointmentStatusKey.setStatusId(statusObject.getId());
        appointmentStatus.setId(appointmentStatusKey);
        appointmentStatus.setAppointment(appointment);
        appointmentStatus.setStatus(statusObject);
        return appointmentStatusRepository.save(appointmentStatus);
    }

    public boolean isTimeSlotAvailable(String doctorId, LocalDate date, int timeSlotFrom, int timeSlotTo) {
        Set<DoctorBusy> doctorBusies = doctorBusyRepository.findByDoctorIdAndDate(doctorId, date);

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
