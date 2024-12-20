package com.dtp.doctor_appointment_booking.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddDoctorRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 3, message = "Name must be at least 3 characters long")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Name cannot contain special characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(min = 3, message = "Email must be at least 3 characters long")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    private int experience;

    @Positive(message = "Fee must be greater than 0")
    private float fees;

    @NotNull(message = "Speciality is required")
    private int speciality;

    @NotBlank(message = "Education is required")
    @Size(min = 3, message = "Education must be at least 3 characters long")
    private String education;

    private String address;
    
    private String about;
}
