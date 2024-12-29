package com.dtp.doctor_appointment_booking.service;

import com.dtp.doctor_appointment_booking.dto.request.BookAppointmentRequest;
import com.dtp.doctor_appointment_booking.model.Appointment;

public interface AppointmentService {
    Appointment bookAppointment(BookAppointmentRequest request);
}
