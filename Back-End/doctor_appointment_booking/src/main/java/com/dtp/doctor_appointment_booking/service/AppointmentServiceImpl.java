package com.dtp.doctor_appointment_booking.service;

import com.dtp.doctor_appointment_booking.dto.appointment.BookAppointmentRequest;
import com.dtp.doctor_appointment_booking.exception.EntityNotFoundException;
import com.dtp.doctor_appointment_booking.exception.SlotUnavailableException;
import com.dtp.doctor_appointment_booking.mapper.AppointmentMapper;
import com.dtp.doctor_appointment_booking.model.*;
import com.dtp.doctor_appointment_booking.model.compositeKey.AppointmentStatusKey;
import com.dtp.doctor_appointment_booking.repository.*;
import com.dtp.doctor_appointment_booking.utils.Schedule;
import com.dtp.doctor_appointment_booking.utils.TimeSlotUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
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
    private final DoctorBusyService doctorBusyService;
    private final AppointmentStatusService appointmentStatusService;
    private final Schedule schedule;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, StatusRepository statusRepository,
                                  AppointmentStatusRepository appointmentStatusRepository, DoctorBusyRepository doctorBusyRepository,
                                  TimeSlotRepository timeSlotRepository, DoctorRepository doctorRepository,
                                  UserRepository userRepository, AppointmentMapper appointmentMapper,
                                  DoctorBusyService doctorBusyService, AppointmentStatusService appointmentStatusService,
                                  Schedule schedule) {
        this.appointmentRepository = appointmentRepository;
        this.statusRepository = statusRepository;
        this.appointmentStatusRepository = appointmentStatusRepository;
        this.doctorBusyRepository = doctorBusyRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
        this.appointmentMapper = appointmentMapper;
        this.doctorBusyService = doctorBusyService;
        this.appointmentStatusService = appointmentStatusService;
        this.schedule = schedule;
    }

    @Transactional
    @Override
    public Appointment bookAppointment(BookAppointmentRequest request) {
        // check valid slot
        if (!TimeSlotUtils.isTimeSlotAvailable(doctorBusyRepository, timeSlotRepository, request.getDoctor_id(),
                request.getDateSlot(), request.getTimeSlotFrom(), request.getTimeSlotTo())) {
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

        // update doctor to be occupied
        doctorBusyService.saveDoctorBusy(appointment, "OCCUPIED");

        // Schedule delete appointment job
        schedule.ScheduleDeleteAppointmentJob(appointment.getAppointment_id());

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

    @Override
    public Appointment updatePaymentAppointment(String appointmentId, String paymentStatus) {
        Appointment appointment = appointmentRepository.findByAppointment_id(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException(Appointment.class));

        appointment.setPaymentStatus(paymentStatus);
        appointmentRepository.save(appointment);

        return appointment;
    }

    @Override
    public Appointment updateAppointmentStatus(String appointmentId, String status) {
        AppointmentStatus appointmentStatus = addAppointmentStatus(appointmentId, status);
        return appointmentStatus.getAppointment();
    }

    @Override
    @Transactional
    public void deleteAppointment(String appointmentId) {
        Appointment appointment = appointmentRepository.findByAppointment_id(appointmentId)
                .orElseThrow(() -> new EntityNotFoundException(Appointment.class));
        // Delete appointment status
        appointmentStatusService.deleteAppointmentStatusByAppointmentId(appointmentId);

        // Delete doctor busy
        DoctorBusy doctorBusy = doctorBusyService.findDoctorBusy(appointment, "OCCUPIED");
        doctorBusyService.deleteDoctorBusy(doctorBusy.getId());

        // Delete appointment
        appointmentRepository.delete(appointment);
    }

    @Override
    public Page<Appointment> getAppointmentByDoctor(String doctorEmail, String patientEmail, LocalDate dateSlot,
                                                    String paymentStatus, String status, boolean dateDesc,
                                                    int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return appointmentRepository.findAllByDoctor(doctorEmail, patientEmail, dateSlot,
                paymentStatus, status, dateDesc, pageable);
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
}
