package com.dtp.doctor_appointment_booking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "doctor_busy")
public class DoctorBusy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "time_slot_from", nullable = false)
    private TimeSlot timeSlotFrom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "time_slot_to", nullable = false)
    private TimeSlot timeSlotTo;

    private LocalDate date;

    private String status;
}
