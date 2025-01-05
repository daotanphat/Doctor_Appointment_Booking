package com.dtp.doctor_appointment_booking.service;

import com.dtp.doctor_appointment_booking.exception.EntityNotFoundException;
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
}
