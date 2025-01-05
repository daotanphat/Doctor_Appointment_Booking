package com.dtp.doctor_appointment_booking.service;

import com.dtp.doctor_appointment_booking.model.AppointmentStatus;
import com.dtp.doctor_appointment_booking.repository.AppointmentStatusRepository;
import org.springframework.stereotype.Service;

@Service
public class AppointmentStatusServiceImpl implements AppointmentStatusService {
    private final AppointmentStatusRepository appointmentStatusRepository;

    public AppointmentStatusServiceImpl(AppointmentStatusRepository appointmentStatusRepository) {
        this.appointmentStatusRepository = appointmentStatusRepository;
    }

    @Override
    public void deleteAppointmentStatusByAppointmentId(String appointmentId) {
        // Delete appointment status
        appointmentStatusRepository.deleteByAppointmentId(appointmentId);
    }
}
