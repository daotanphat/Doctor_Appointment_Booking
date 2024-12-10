package com.dtp.doctor_appointment_booking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "appointment")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String appointment_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private User patient;

    private float fee;

    private LocalDate dateSlot;

    private String timeSlot;

    private String address;

    @OneToMany(mappedBy = "appointment", cascade = CascadeType.ALL)
    private Set<AppointmentStatus> statuses = new HashSet<>();

    private String paymentStatus;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @PrePersist
    public void generateAppointmentId() {
        this.appointment_id = UUID.randomUUID().toString();
    }
}
