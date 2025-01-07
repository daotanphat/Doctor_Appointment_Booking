package com.dtp.doctor_appointment_booking.service;

import com.dtp.doctor_appointment_booking.dto.appointment.BookAppointmentRequest;
import com.dtp.doctor_appointment_booking.model.Appointment;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

public interface AppointmentService {
    Appointment bookAppointment(BookAppointmentRequest request);

    Page<Appointment> getMyAppointments(String email, String search, int page, int size);

    Appointment cancelAppointment(String appointmentId);

    Appointment getAppointment(String appointmentId);

    Appointment updatePaymentAppointment(String appointmentId, String paymentStatus);

    Appointment updateAppointmentStatus(String appointmentId, String status);

    void deleteAppointment(String appointmentId);

    Page<Appointment> getAppointmentByDoctor(String doctorEmail, String patientEmail, LocalDate dateSlot,
                                             String paymentStatus, String status, boolean dateDesc, int page, int size);
}
