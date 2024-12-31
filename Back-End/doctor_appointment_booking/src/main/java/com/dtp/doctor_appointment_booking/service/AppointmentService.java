package com.dtp.doctor_appointment_booking.service;

import com.dtp.doctor_appointment_booking.dto.appointment.BookAppointmentRequest;
import com.dtp.doctor_appointment_booking.model.Appointment;
import org.springframework.data.domain.Page;

public interface AppointmentService {
    Appointment bookAppointment(BookAppointmentRequest request);

    Page<Appointment> getMyAppointments(String email, String search, int page, int size);

    Appointment cancelAppointment(String appointmentId);

    Appointment getAppointment(String appointmentId);
}
