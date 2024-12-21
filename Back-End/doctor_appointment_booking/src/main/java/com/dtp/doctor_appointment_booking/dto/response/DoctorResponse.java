package com.dtp.doctor_appointment_booking.dto.response;

import com.dtp.doctor_appointment_booking.model.Appointment;
import com.dtp.doctor_appointment_booking.model.Feedback;
import com.dtp.doctor_appointment_booking.model.Speciality;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorResponse {
    private String doctorId;
    private String name;
    private String email;
    private LocalDate dateOfBirth;
    private String phone;
    private String address;
    private boolean gender;
    private String imageUrl;
    private boolean status;
    private int experience;
    private String degree;
    private String speciality;
    private float fee;
    private String description;
    private String bankNumber;
    private float rating;
}
