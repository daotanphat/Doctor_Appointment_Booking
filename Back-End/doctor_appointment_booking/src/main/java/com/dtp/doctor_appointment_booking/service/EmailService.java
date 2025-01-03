package com.dtp.doctor_appointment_booking.service;

import com.dtp.doctor_appointment_booking.dto.email.EmailDetails;
import com.dtp.doctor_appointment_booking.model.Appointment;

public interface EmailService {
    void sendAppointmentNotification(Appointment appointment);
}
