package com.dtp.doctor_appointment_booking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String email;
    private String fullName;
    private LocalDate dateOfBirth;
    private String phone;
    private String address;
    private boolean gender;
    private String imageUrl;
    private LocalDate createdAt;
}
