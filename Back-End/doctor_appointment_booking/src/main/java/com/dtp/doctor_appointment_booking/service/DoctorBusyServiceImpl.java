package com.dtp.doctor_appointment_booking.service;

import com.dtp.doctor_appointment_booking.model.Appointment;
import com.dtp.doctor_appointment_booking.model.DoctorBusy;
import com.dtp.doctor_appointment_booking.repository.DoctorBusyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class DoctorBusyServiceImpl implements DoctorBusyService {
    private final DoctorBusyRepository doctorBusyRepository;

    public DoctorBusyServiceImpl(DoctorBusyRepository doctorBusyRepository) {
        this.doctorBusyRepository = doctorBusyRepository;
    }

    @Override
    public DoctorBusy saveDoctorBusy(Appointment appointment) {
        DoctorBusy doctorBusy = new DoctorBusy();
        doctorBusy.setDoctor(appointment.getDoctor());
        doctorBusy.setTimeSlotFrom(appointment.getTimeSlotFrom());
        doctorBusy.setTimeSlotTo(appointment.getTimeSlotTo());
        doctorBusy.setDate(appointment.getDateSlot());
        return doctorBusyRepository.save(doctorBusy);
    }
}
