package com.dtp.doctor_appointment_booking.service;

import com.dtp.doctor_appointment_booking.dto.DoctorBusy.request.CreateDoctorBusyRequest;
import com.dtp.doctor_appointment_booking.exception.SlotUnavailableException;
import com.dtp.doctor_appointment_booking.model.Appointment;
import com.dtp.doctor_appointment_booking.model.Doctor;
import com.dtp.doctor_appointment_booking.model.DoctorBusy;
import com.dtp.doctor_appointment_booking.model.TimeSlot;
import com.dtp.doctor_appointment_booking.repository.DoctorBusyRepository;
import com.dtp.doctor_appointment_booking.repository.TimeSlotRepository;
import com.dtp.doctor_appointment_booking.utils.TimeSlotUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class DoctorBusyServiceImpl implements DoctorBusyService {
    private final DoctorBusyRepository doctorBusyRepository;
    private final DoctorService doctorService;
    private final TimeSlotService timeSlotService;
    private final TimeSlotRepository timeSlotRepository;

    public DoctorBusyServiceImpl(DoctorBusyRepository doctorBusyRepository, DoctorService doctorService,
                                 TimeSlotService timeSlotService, TimeSlotRepository timeSlotRepository) {
        this.doctorBusyRepository = doctorBusyRepository;
        this.doctorService = doctorService;
        this.timeSlotService = timeSlotService;
        this.timeSlotRepository = timeSlotRepository;
    }

    @Override
    public DoctorBusy saveDoctorBusy(Appointment appointment, String status) {
        DoctorBusy doctorBusy = new DoctorBusy();
        doctorBusy.setDoctor(appointment.getDoctor());
        doctorBusy.setTimeSlotFrom(appointment.getTimeSlotFrom());
        doctorBusy.setTimeSlotTo(appointment.getTimeSlotTo());
        doctorBusy.setDate(appointment.getDateSlot());
        doctorBusy.setStatus(status);
        return doctorBusyRepository.save(doctorBusy);
    }

    @Override
    public DoctorBusy findDoctorBusy(Appointment appointment, String status) {
        return doctorBusyRepository.findDoctorOccupied(appointment.getDoctor().getDoctor_id(),
                appointment.getDateSlot(), appointment.getTimeSlotFrom().getTime(),
                appointment.getTimeSlotTo().getTime(), status);
    }

    @Override
    public void deleteDoctorBusy(int doctorBusyId) {
        doctorBusyRepository.deleteById(doctorBusyId);
    }

    @Override
    public DoctorBusy saveDoctorBusy(CreateDoctorBusyRequest request) {
        Doctor doctor = doctorService.getDoctorByEmail(request.getDoctorEmail());
        TimeSlot timeSlotFrom = timeSlotService.getTimeSlot(request.getTimeFrom());
        TimeSlot timeSlotTo = timeSlotService.getTimeSlot(request.getTimeTo());

        // check valid slot
        if (!TimeSlotUtils.isTimeSlotAvailable(doctorBusyRepository, timeSlotRepository, doctor.getDoctor_id(),
                request.getDate(), request.getTimeFrom(), request.getTimeTo())) {
            throw new SlotUnavailableException("Slot not available");
        }


        DoctorBusy doctorBusy = new DoctorBusy();
        doctorBusy.setDoctor(doctor);
        doctorBusy.setTimeSlotFrom(timeSlotFrom);
        doctorBusy.setTimeSlotTo(timeSlotTo);
        doctorBusy.setDate(request.getDate());
        doctorBusy.setStatus("BUSY");
        return doctorBusyRepository.save(doctorBusy);
    }
}
