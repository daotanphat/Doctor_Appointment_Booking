package com.dtp.doctor_appointment_booking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "doctor")
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String doctor_id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(nullable = false)
    private String fullName;

    private LocalDate dateOfBirth;

    private String phone;

    private String address;

    private boolean gender;

    private String imageUrl;

    private boolean status;

    private int experience;

    private String degree;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonManagedReference
    @JoinColumn(name = "speciality_id", nullable = false)
    private Speciality speciality;

    private float fee;

    private String description;

    private String bankNumber;

    private float rating;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private Set<Appointment> appointments = new HashSet<>();

    @PrePersist
    public void generateDoctorId() {
        this.doctor_id = UUID.randomUUID().toString();
    }

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL)
    private Set<Feedback> feedbacks = new HashSet<>();
}
