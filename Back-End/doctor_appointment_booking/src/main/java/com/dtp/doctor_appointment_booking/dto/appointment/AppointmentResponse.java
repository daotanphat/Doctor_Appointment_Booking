package com.dtp.doctor_appointment_booking.dto.appointment;

import com.dtp.doctor_appointment_booking.model.AppointmentStatus;
import com.dtp.doctor_appointment_booking.model.Doctor;
import com.dtp.doctor_appointment_booking.model.TimeSlot;
import com.dtp.doctor_appointment_booking.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentResponse {
    private String appointment_id;
    private String doctor;
    private String patient;
    private float fee;
    private LocalDate dateSlot;
    private LocalTime timeSlotFrom;
    private LocalTime timeSlotTo;
    private String address;
    private String status;
    private String paymentStatus;
    private LocalDateTime createdAt;
}
