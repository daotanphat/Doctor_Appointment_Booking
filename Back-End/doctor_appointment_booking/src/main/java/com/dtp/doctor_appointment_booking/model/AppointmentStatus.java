package com.dtp.doctor_appointment_booking.model;

import com.dtp.doctor_appointment_booking.model.compositeKey.AppointmentStatusKey;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "appointment_status")
public class AppointmentStatus {

    @EmbeddedId
    private AppointmentStatusKey id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("appointmentId")
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("statusId")
    @JoinColumn(name = "status_id")
    private Status status;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
